package com.olt.racketclash.screens.games

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Bye
import com.olt.racketclash.data.FileHandler
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.Round
import com.olt.racketclash.database.Database
import com.olt.racketclash.language.translations.Language
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GamesModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database,
    private val fileHandler: FileHandler,
    language: Language
) : NavigableStateScreenModel<GamesModel.Model>(navigateToScreen, Model(language = language)) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.rounds().collect {
                updateState { copy(rounds = it) }
            }
        }
        screenModelScope.launch(context = Dispatchers.IO) {
            database.games().collect {
                updateState { copy(games = it.groupBy { it.roundId }, active = activeGames(games = it, timeout = timeout, fields = fields)) }
            }
        }
        screenModelScope.launch(context = Dispatchers.IO) {
            database.bye().collect {
                updateState { copy(bye = it.groupBy { it.roundId }) }
            }
        }
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.fields().collect {
                updateState { copy(fields = it, active = activeGames(games = games.values.flatten(), timeout = timeout, fields = it)) }
            }
        }
        screenModelScope.launch(context = Dispatchers.IO) {
            fileHandler.timeout().collect {
                updateState { copy(timeout = it, active = activeGames(games = games.values.flatten(), timeout = it, fields = fields)) }
            }
        }

        screenModelScope.launch(context = Dispatchers.IO) {
            while (isActive) {
                updateState {
                    copy(active = activeGames(games = games.values.flatten(), timeout = timeout, fields = fields))
                }
                Thread.sleep(10_000)
            }
        }
    }

    data class Model(
        val language: Language,
        val fields: Int = 1,
        val timeout: Int = 1,
        val rounds: List<Round> = emptyList(),
        val games: Map<Long, List<Game>> = emptyMap(),
        val bye: Map<Long, List<Bye>> = emptyMap(),
        val active: List<Long> = emptyList()
    )

    fun deleteRound(id: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.deleteRound(id = id)
        }
    }

    fun setDone(
        game: Game,
        isDone: Boolean
    ) {
        screenModelScope.launch(context = Dispatchers.IO) {
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
    }

    fun changeSet(roundId: Long, gameId: Long, setNumber: Int, isLeft: Boolean, text: String) {
        screenModelScope.launch(context = Dispatchers.Default) {
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
    }

    fun changeFields(newFields: Int) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (newFields >= 1) fileHandler.setFields(newFields = newFields)
        }
    }

    fun changeTimeout(newTimeout: Int) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (newTimeout >= 1) fileHandler.setTimeout(newTimeout = newTimeout)
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