package com.olt.racketclash.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.data.Game
import com.olt.racketclash.database.mapper.toGame
import kotlinx.coroutines.flow.Flow

class GamesDatabase(private val queries: GameQueries) {

    companion object {
        val gameAdapter by lazy {
            GameTable.Adapter(
                set1LeftAdapter = IntColumnAdapter, set1RightAdapter = IntColumnAdapter,
                set2LeftAdapter = IntColumnAdapter, set2RightAdapter = IntColumnAdapter,
                set3LeftAdapter = IntColumnAdapter, set3RightAdapter = IntColumnAdapter,
                set4LeftAdapter = IntColumnAdapter, set4RightAdapter = IntColumnAdapter,
                set5LeftAdapter = IntColumnAdapter, set5RightAdapter = IntColumnAdapter
            )
        }
    }

    fun games(): Flow<List<Game>> =
        queries
            .selectAll()
            .mapToList { it.toGame() }

    fun games(roundId: Long): Flow<List<Game>> =
        queries
            .selectAllInRound(roundId = roundId)
            .mapToList { it.toGame() }

    fun select(id: Long): GameTable? = queries.select(id = id).executeAsOneOrNull()

    fun addGame(
        roundId: Long?,
        playerLeft1Id: Long?, playerLeft2Id: Long?,
        playerRight1Id: Long?, playerRight2Id: Long?
    ) =
        roundId?.let {
            queries.add(
                roundId = it,
                playerLeft1Id = playerLeft1Id, playerLeft2Id = playerLeft2Id,
                playerRight1Id = playerRight1Id, playerRight2Id = playerRight2Id
            )
        }

    fun deleteGame(id: Long) = queries.delete(id = id)

    fun updateGame(
        id: Long, isDone: Boolean,
        set1Left: Int, set1Right: Int,
        set2Left: Int, set2Right: Int,
        set3Left: Int, set3Right: Int,
        set4Left: Int, set4Right: Int,
        set5Left: Int, set5Right: Int
    ) =
        queries.update(
            id = id, isDone = isDone,
            set1Left = set1Left, set1Right = set1Right,
            set2Left = set2Left, set2Right = set2Right,
            set3Left = set3Left, set3Right = set3Right,
            set4Left = set4Left, set4Right = set4Right,
            set5Left = set5Left, set5Right = set5Right
        )
}