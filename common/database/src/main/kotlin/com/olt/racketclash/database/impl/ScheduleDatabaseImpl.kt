package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.database.api.GameSet
import com.olt.racketclash.database.api.Schedule
import com.olt.racketclash.database.api.ScheduleDatabase
import com.olt.racketclash.database.api.ScheduleFilter
import com.olt.racketclash.database.api.ScheduleSorting
import kotlin.math.min

internal class ScheduleDatabaseImpl : ScheduleDatabase {
    private val schedules = mutableListOf<Schedule>()

    override suspend fun selectList(
        filter: ScheduleFilter,
        sorting: ScheduleSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Schedule, ScheduleFilter, ScheduleSorting> =
        FilteredSortedList(
            totalSize = schedules.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = schedules.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), schedules.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectFirst(n: Long): List<Schedule> =
        schedules.take(n.toInt())

    override suspend fun setComplete(
        schedule: Schedule,
        sets: List<GameSet>
    ) {}

    override suspend fun add(schedule: Schedule) {
        schedules.add(schedule)
    }
}