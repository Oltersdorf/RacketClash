package com.olt.racketclash.data.database

import com.olt.racketclash.data.Bye
import com.olt.racketclash.data.Game
import kotlinx.coroutines.flow.Flow

interface IGameDatabase {
    fun games(): Flow<List<Game>>
    fun games(roundId: Long): Flow<List<Game>>
    fun add(
        roundId: Long,
        playerLeft1Id: Long?, playerLeft2Id: Long?,
        playerRight1Id: Long?, playerRight2Id: Long?,
        projectId: Long
    )
    fun update(
        id: Long, isDone: Boolean,
        set1Left: Int, set1Right: Int,
        set2Left: Int, set2Right: Int,
        set3Left: Int, set3Right: Int,
        set4Left: Int, set4Right: Int,
        set5Left: Int, set5Right: Int,
        projectId: Long
    )
    fun delete(id: Long, projectId: Long)
    fun addRoundsWithGames(
        rounds: Map<String, List<Game>>,
        bye: List<Bye>,
        projectId: Long
    )
}