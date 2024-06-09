package com.olt.racketclash.app.screens.games

import com.olt.racketclash.data.Bye
import com.olt.racketclash.app.RacketClashModel
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.state.ViewModelState
import kotlinx.coroutines.isActive

class GamesModel(
    private val database: Database,
    private val appModel: RacketClashModel
) : ViewModelState<GamesModel.State>(initialState = State()) {

    init {
        onIO {
            appModel.state.collect {
                val project = it.currentProject

                updateState {
                    if (project != null)
                        copy(
                            fields = project.fields,
                            timeout = project.timeout,
                            gamePointsForBye = project.gamePointsForBye,
                            setPointsForBye = project.setPointsForBye,
                            pointsForBye = project.pointsForBye
                        )
                    else
                        this
                }
            }
        }

        onIO {
            database.rounds().collect {
                updateState { copy(rounds = it) }
            }
        }

        onIO {
            database.games().collect {
                updateState { copy(games = it.groupBy { it.roundId }, active = activeGames(games = it, timeout = timeout, fields = fields)) }
            }
        }

        onIO {
            database.bye().collect {
                updateState { copy(bye = it.groupBy { it.roundId }) }
            }
        }

        onDefault {
            while (isActive) {
                updateState {
                    copy(active = activeGames(games = games.values.flatten(), timeout = timeout, fields = fields))
                }
                Thread.sleep(10_000)
            }
        }
    }

    data class State(
        val fields: Int = 1,
        val timeout: Int = 1,
        val gamePointsForBye: Int = 0,
        val setPointsForBye: Int = 0,
        val pointsForBye: Int = 0,
        val rounds: List<Round> = emptyList(),
        val games: Map<Long, List<Game>> = emptyMap(),
        val bye: Map<Long, List<Bye>> = emptyMap(),
        val active: List<Long> = emptyList()
    )

    fun deleteRound(id: Long) =
        onIO {
            database.deleteRound(id = id)
        }

    fun setDone(
        game: Game,
        isDone: Boolean
    ) =
        onIO {
            database.updateGame(
                id = game.id,
                set1Left = game.set1Left,
                set1Right = game.set1Right,
                set2Left = game.set2Left,
                set2Right = game.set2Right,
                set3Left = game.set3Left,
                set3Right = game.set3Right,
                set4Left = game.set4Left,
                set4Right = game.set4Right,
                set5Left = game.set5Left,
                set5Right = game.set5Right,
                isDone = isDone
            )
        }

    fun changeSet(roundId: Long, gameId: Long, setNumber: Int, isLeft: Boolean, text: String) =
        onDefault {
            updateState {
                val roundGames = games[roundId]?.toMutableList()
                val gameIndex = roundGames?.indexOfFirst { it.id == gameId }

                val number = text.toIntOrNull()

                if (gameIndex == null || number == null || number >= 100 || number < 0) this
                else {
                    val modifiedGame = if (isLeft)
                        when (setNumber) {
                            1 -> roundGames[gameIndex].copy(set1Left = number)
                            2 -> roundGames[gameIndex].copy(set2Left = number)
                            3 -> roundGames[gameIndex].copy(set3Left = number)
                            4 -> roundGames[gameIndex].copy(set4Left = number)
                            5 -> roundGames[gameIndex].copy(set5Left = number)
                            else -> null
                        }
                    else {
                        when (setNumber) {
                            1 -> roundGames[gameIndex].copy(set1Right = number)
                            2 -> roundGames[gameIndex].copy(set2Right = number)
                            3 -> roundGames[gameIndex].copy(set3Right = number)
                            4 -> roundGames[gameIndex].copy(set4Right = number)
                            5 -> roundGames[gameIndex].copy(set5Right = number)
                            else -> null
                        }
                    }

                    if (modifiedGame == null) this
                    else {
                        roundGames[gameIndex] = modifiedGame
                        val games = games.toMutableMap()
                        games[roundId] = roundGames.toList()

                        copy(games = games.toMap())
                    }
                }
            }
        }

    fun changeFields(newFields: Int) =
        onDefault {
            if (newFields >= 1) {
                onIO { appModel.setFields(newFields = newFields) }
            }
        }

    fun changeTimeout(newTimeout: Int) =
        onDefault {
            if (newTimeout >= 1) {
                onIO { appModel.setTimeout(newTimeout = newTimeout) }
            }
        }

    fun changeGamePointsForBye(newGamePointsForBye: Int) =
        onDefault {
            if (newGamePointsForBye >= 0) {
                onIO { appModel.setGamePointsForBye(newGamePointsForBye = newGamePointsForBye) }
            }
        }

    fun changeSetPointsForBye(newSetPointsForBye: Int) =
        onDefault {
            if (newSetPointsForBye >= 0) {
                onIO { appModel.setSetPointsForBye(newSetPointsForBye = newSetPointsForBye) }
            }
        }

    fun changePointsForBye(newPointsForBye: Int) =
        onDefault {
            if (newPointsForBye >= 0) {
                onIO { appModel.setPointsForBye(newPointsForBye = newPointsForBye) }
            }
        }

    private fun activeGames(games: List<Game>, timeout: Int, fields: Int): List<Long> {
        val timeoutInMillis = System.currentTimeMillis() - (timeout * 60000L)
        val activeGames = mutableListOf<Long>()
        val activePlayers = mutableListOf<Long>()

        games
            .asSequence()
            .filterNot { it.isDone }
            .filter { (it.playerLeft1LastPlayed ?: Long.MIN_VALUE) <= timeoutInMillis }
            .filter { (it.playerLeft2LastPlayed ?: Long.MIN_VALUE) <= timeoutInMillis }
            .filter { (it.playerRight1LastPlayed ?: Long.MIN_VALUE) <= timeoutInMillis }
            .filter { (it.playerRight2LastPlayed ?: Long.MIN_VALUE) <= timeoutInMillis }
            .forEach { game ->
                if (
                    !activePlayers.contains(game.playerLeft1Id) &&
                    !activePlayers.contains(game.playerLeft2Id) &&
                    !activePlayers.contains(game.playerRight1Id) &&
                    !activePlayers.contains(game.playerRight2Id)
                ) {
                    activeGames.add(game.id)
                    if (activeGames.size == fields)
                        return activeGames.toList()

                    game.playerLeft1Id?.let { activePlayers.add(it) }
                    game.playerLeft2Id?.let { activePlayers.add(it) }
                    game.playerRight1Id?.let { activePlayers.add(it) }
                    game.playerRight2Id?.let { activePlayers.add(it) }
                }
            }

        return activeGames.toList()
    }
}