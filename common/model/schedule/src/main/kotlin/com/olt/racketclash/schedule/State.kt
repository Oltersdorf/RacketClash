package com.olt.racketclash.schedule

import com.olt.racketclash.database.api.Schedule
import com.olt.racketclash.database.api.ScheduleSorting

data class State(
    val isLoading: Boolean = true,
    val scheduledGames: List<Schedule> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(active = true, singles = true),
    val tags: Tags = Tags(),
    val sorting: ScheduleSorting = ScheduleSorting.ScheduleAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)