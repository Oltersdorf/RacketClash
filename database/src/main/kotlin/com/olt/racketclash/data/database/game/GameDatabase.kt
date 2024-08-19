package com.olt.racketclash.data.database.game

import com.olt.racketclash.data.Bye
import com.olt.racketclash.data.Game
import com.olt.racketclash.data.database.IGameDatabase
import com.olt.racketclash.data.database.TimeStamp
import com.olt.racketclash.data.database.mapToList
import com.olt.racketclash.database.RacketClashDatabase
import kotlinx.coroutines.flow.Flow

internal class GameDatabase(
    private val database: RacketClashDatabase
): TimeStamp(), IGameDatabase {

    override fun games(): Flow<List<Game>> =
        database
            .gameQueries
            .selectAll()
            .mapToList { it.toGame() }

    override fun games(roundId: Long): Flow<List<Game>> =
        database
            .gameQueries
            .selectAllInRound(roundId = roundId)
            .mapToList { it.toGame() }

    override fun add(
        roundId: Long,
        playerLeft1Id: Long?, playerLeft2Id: Long?,
        playerRight1Id: Long?, playerRight2Id: Long?,
        projectId: Long
    ) {
        database.transaction {
            database.gameQueries.add(
                roundId = roundId,
                playerLeft1Id = playerLeft1Id, playerLeft2Id = playerLeft2Id,
                playerRight1Id = playerRight1Id, playerRight2Id = playerRight2Id
            )
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }
    }

    override fun update(
        id: Long, isDone: Boolean,
        set1Left: Int, set1Right: Int,
        set2Left: Int, set2Right: Int,
        set3Left: Int, set3Right: Int,
        set4Left: Int, set4Right: Int,
        set5Left: Int, set5Right: Int,
        projectId: Long
    ) {
        database.transaction {
            database.gameQueries.update(
                id = id, isDone = isDone,
                set1Left = set1Left, set1Right = set1Right,
                set2Left = set2Left, set2Right = set2Right,
                set3Left = set3Left, set3Right = set3Right,
                set4Left = set4Left, set4Right = set4Right,
                set5Left = set5Left, set5Right = set5Right
            )

            val game = database.gameQueries.select(id = id).executeAsOneOrNull()

            if (game != null) {
                val lastPlayed = if (isDone) System.currentTimeMillis() else null
                game.playerLeft1Id?.let { database.playerQueries.setLastPlayed(id = it, lastPlayed = lastPlayed) }
                game.playerLeft2Id?.let { database.playerQueries.setLastPlayed(id = it, lastPlayed = lastPlayed) }
                game.playerRight1Id?.let { database.playerQueries.setLastPlayed(id = it, lastPlayed = lastPlayed) }
                game.playerRight2Id?.let { database.playerQueries.setLastPlayed(id = it, lastPlayed = lastPlayed) }
            }

            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }
    }

    override fun delete(id: Long, projectId: Long) =
        database.transaction {
            database.gameQueries.delete(id = id)
            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }

    override fun addRoundsWithGames(
        rounds: Map<String, List<Game>>,
        bye: List<Bye>,
        projectId: Long
    ) {
        database.transaction {
            rounds.onEachIndexed { index, (key, value) ->
                database.roundQueries.add(name = key)
                val roundId = database.roundQueries.lastInsertRowId().executeAsOneOrNull() ?: this.rollback()

                value.forEach { game ->
                    database.gameQueries.add(
                        roundId = roundId,
                        playerLeft1Id = game.playerLeft1Id, playerLeft2Id = game.playerLeft2Id,
                        playerRight1Id = game.playerRight1Id, playerRight2Id = game.playerRight2Id
                    )
                }

                bye.filter { it.roundId == index.toLong() + 1 }.forEach { bye ->
                    database.byeQueries.add(roundId = roundId, playerId = bye.playerId)
                }
            }

            database.projectQueries.updateLastModified(id = projectId, lastModified = currentTime())
        }
    }
}