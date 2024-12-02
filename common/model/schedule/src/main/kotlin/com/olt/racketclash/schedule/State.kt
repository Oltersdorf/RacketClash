package com.olt.racketclash.schedule

import com.olt.racketclash.database.schedule.Schedule
import com.olt.racketclash.database.schedule.Sorting

data class State(
    val isLoading: Boolean = true,
    val scheduledGames: List<Schedule> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(active = true, singles = true),
    val tags: Tags = Tags(),
    val sorting: Sorting = Sorting.ScheduleAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)