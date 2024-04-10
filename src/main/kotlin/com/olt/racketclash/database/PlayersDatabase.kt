package com.olt.racketclash.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.data.Player
import com.olt.racketclash.database.mapper.toPlayer
import kotlinx.coroutines.flow.Flow

class PlayersDatabase(private val queries: PlayerQueries) {

    companion object {
        val playerAdapter by lazy {
            PlayerTable.Adapter(
                openGamesAdapter = IntColumnAdapter, playedAdapter = IntColumnAdapter,
                byeAdapter = IntColumnAdapter,
                wonGamesAdapter = IntColumnAdapter, lostGamesAdapter = IntColumnAdapter,
                wonSetsAdapter = IntColumnAdapter, lostSetsAdapter = IntColumnAdapter,
                wonPointsAdapter = IntColumnAdapter, lostPointsAdapter = IntColumnAdapter
            )
        }
    }

    fun players(): Flow<List<Player>> =
        queries
            .selectAll()
            .mapToList { it.toPlayer() }

    fun activePlayers(): Flow<List<Player>> =
        queries
            .selectActive()
            .mapToList { it.toPlayer() }

    private fun size(): Int =
        queries
            .size()
            .executeAsOneOrNull()
            ?.toInt() ?: 0

    fun addPlayer(name: String, teamId: Long): Int {
        queries.add(name = name, teamId = teamId)
        return size()
    }

    fun updatePlayer(id: Long, name: String, teamId: Long) = queries.update(id = id, name = name, teamId = teamId)

    fun setActive(id: Long, active: Boolean) = queries.setActive(id = id, active = active)

    fun deletePlayer(id: Long): Int {
        queries.delete(id = id)
        return size()
    }

    fun addUndoneGame(id: Long?) = id?.let { queries.addUndoneGame(id = it) }

    fun addByeGame(id: Long?) = id?.let { queries.addByeGame(id = it) }

    fun updateDone(game: GameTable) {
        val stats = game.toGameStats()

        if (game.isDone) {
            game.playerLeft1Id?.let { setGameDone(it, isLeft = true, stats = stats) }
            game.playerLeft2Id?.let { setGameDone(it, isLeft = true, stats = stats) }
            game.playerRight1Id?.let { setGameDone(it, isLeft = false, stats = stats) }
            game.playerRight2Id?.let { setGameDone(it, isLeft = false, stats = stats) }
        } else {
            game.playerLeft1Id?.let { setGameUndone(it, isLeft = true, stats = stats) }
            game.playerLeft2Id?.let { setGameUndone(it, isLeft = true, stats = stats) }
            game.playerRight1Id?.let { setGameUndone(it, isLeft = false, stats = stats) }
            game.playerRight2Id?.let { setGameUndone(it, isLeft = false, stats = stats) }
        }
    }

    private fun setGameDone(id: Long, isLeft: Boolean, stats: GameStats) =
        queries.setGameDone(
            id = id,
            wonGame = if (isLeft) stats.leftWon else stats.rightWon,
            lostGame = if (isLeft) stats.rightWon else stats.leftWon,
            wonSets = if (isLeft) stats.leftWonSets else stats.rightWonSets,
            lostSets = if (isLeft) stats.rightWonSets else stats.leftWonSets,
            wonPoints = if (isLeft) stats.leftPoints else stats.rightPoints,
            lostPoints = if (isLeft) stats.rightPoints else stats.leftPoints
        )

    private fun setGameUndone(id: Long, isLeft: Boolean, stats: GameStats) =
        queries.setGameUndone(
            id = id,
            wonGame = if (isLeft) stats.leftWon else stats.rightWon,
            lostGame = if (isLeft) stats.rightWon else stats.leftWon,
            wonSets = if (isLeft) stats.leftWonSets else stats.rightWonSets,
            lostSets = if (isLeft) stats.rightWonSets else stats.leftWonSets,
            wonPoints = if (isLeft) stats.leftPoints else stats.rightPoints,
            lostPoints = if (isLeft) stats.rightPoints else stats.leftPoints
        )

    fun deleteGame(game: GameTable) {
        val stats = game.toGameStats()

        if (game.isDone) {
            game.playerLeft1Id?.let { removeDoneGame(it, isLeft = true, stats = stats) }
            game.playerLeft2Id?.let { removeDoneGame(it, isLeft = true, stats = stats) }
            game.playerRight1Id?.let { removeDoneGame(it, isLeft = false, stats = stats) }
            game.playerRight2Id?.let { removeDoneGame(it, isLeft = false, stats = stats) }
        } else {
            removeUndoneGame(id = game.playerLeft1Id)
            removeUndoneGame(id = game.playerLeft2Id)
            removeUndoneGame(id = game.playerRight1Id)
            removeUndoneGame(id = game.playerRight2Id)
        }
    }

    fun deleteBye(playerId: Long) = removeByeGame(id = playerId)

    private fun removeByeGame(id: Long?) = id?.let { queries.removeByeGame(id = id) }

    private fun removeDoneGame(id: Long, isLeft: Boolean, stats: GameStats) =
        queries.removeDoneGame(
            id = id,
            wonGame = if (isLeft) stats.leftWon else stats.rightWon,
            lostGame = if (isLeft) stats.rightWon else stats.leftWon,
            wonSets = if (isLeft) stats.leftWonSets else stats.rightWonSets,
            lostSets = if (isLeft) stats.rightWonSets else stats.leftWonSets,
            wonPoints = if (isLeft) stats.leftPoints else stats.rightPoints,
            lostPoints = if (isLeft) stats.rightPoints else stats.leftPoints
        )

    private fun removeUndoneGame(id: Long?) = id?.let { queries.removeUndoneGame(id = it) }

    private data class GameStats(
        val leftWon: Int,
        val rightWon: Int,
        val leftWonSets: Int,
        val rightWonSets: Int,
        val leftPoints: Int,
        val rightPoints: Int
    )

    private fun GameTable.toGameStats(): GameStats {
        val leftWonSets = listOf(
            if (set1Left - set1Right > 0) 1 else 0,
            if (set2Left - set2Right > 0) 1 else 0,
            if (set3Left - set3Right > 0) 1 else 0,
            if (set4Left - set4Right > 0) 1 else 0,
            if (set5Left - set5Right > 0) 1 else 0
        ).sum()
        val rightWonSets = listOf(
            if (set1Left - set1Right < 0) 1 else 0,
            if (set2Left - set2Right < 0) 1 else 0,
            if (set3Left - set3Right < 0) 1 else 0,
            if (set4Left - set4Right < 0) 1 else 0,
            if (set5Left - set5Right < 0) 1 else 0
        ).sum()

        return GameStats(
            leftWon = if (leftWonSets > rightWonSets) 1 else 0,
            rightWon = if (leftWonSets < rightWonSets) 1 else 0,
            leftWonSets = leftWonSets,
            rightWonSets = rightWonSets,
            leftPoints = set1Left + set2Left + set3Left + set4Left + set5Left,
            rightPoints = set1Right + set2Right + set3Right + set4Right + set5Right
        )
    }
}