package com.olt.racketclash.database.api

import java.time.Instant

data class Schedule(
    val id: Long = -1,
    val ruleId: Long = -1,
    val ruleName: String = "",
    val maxSets: Int = 0,
    val scheduledFor: Instant = Instant.EPOCH,
    val active: Boolean = false,
    val categoryId: Long = -1,
    val categoryName: String = "",
    val categoryOrderNumber: Int = 0,
    val tournamentId: Long = -1,
    val playerIdLeftOne: Long = -1,
    val playerNameLeftOne: String = "",
    val playerIdLeftTwo: Long? = null,
    val playerNameLeftTwo: String? = null,
    val playerIdRightOne: Long = -1,
    val playerNameRightOne: String = "",
    val playerIdRightTwo: Long? = null,
    val playerNameRightTwo: String? = null
)

data class ScheduleFilter(
    val tournamentId: Long = -1,
    val categoryName: String = "",
    val isSingle: Boolean? = null,
    val isActive: Boolean? = null,
    val playerName: String = ""
)

sealed interface ScheduleSorting {
    data object ActiveAsc : ScheduleSorting
    data object ActiveDesc : ScheduleSorting
    data object ScheduleAsc : ScheduleSorting
    data object ScheduleDesc : ScheduleSorting
    data object TypeAsc : ScheduleSorting
    data object TypeDesc : ScheduleSorting
    data object CategoryAsc : ScheduleSorting
    data object CategoryDesc : ScheduleSorting
}

interface ScheduleDatabase {

    suspend fun selectList(
        filter: ScheduleFilter,
        sorting: ScheduleSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Schedule, ScheduleFilter, ScheduleSorting>

    suspend fun selectFirst(n: Long): List<Schedule>

    suspend fun setComplete(schedule: Schedule, sets: List<GameSet>)

    suspend fun add(schedule: Schedule)
}