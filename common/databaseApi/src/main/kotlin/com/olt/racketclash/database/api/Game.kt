package com.olt.racketclash.database.api

import java.time.Instant

data class Game(
    val id: Long? = -1,
    val scheduleId: Long? = -1,
    val scheduled: Instant = Instant.EPOCH,
    val submitted: Instant? = null,
    val playerIdLeftOne: Long = -1,
    val playerNameLeftOne: String = "",
    val playerTeamIdLeftOne: Long? = null,
    val playerTeamNameLeftOne: String? = null,
    val playerIdLeftTwo: Long? = null,
    val playerNameLeftTwo: String? = null,
    val playerTeamIdLeftTwo: Long? = null,
    val playerTeamNameLeftTwo: String? = null,
    val playerIdRightOne: Long? = null,
    val playerNameRightOne: String? = null,
    val playerTeamIdRightOne: Long? = null,
    val playerTeamNameRightOne: String? = null,
    val playerIdRightTwo: Long? = null,
    val playerNameRightTwo: String? = null,
    val playerTeamIdRightTwo: Long? = null,
    val playerTeamNameRightTwo: String? = null,
    val sets: List<GameSet> = emptyList(),
    val leftWon: Boolean? =
        if (sets.isEmpty())
            null
        else
            sets.count { it.leftPoints > it.rightPoints } > sets.count { it.leftPoints < it.rightPoints }
)

data class GameSet(
    val id: Long = -1,
    val gameId: Long = 0,
    val orderNumber: Int = 0,
    val leftPoints: Int = 0,
    val rightPoints: Int = 0
)

interface GameDatabase {

    suspend fun selectList(
        categoryId: Long,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Game, *, *>

    suspend fun selectLast(n: Long): List<Game>
}