package com.olt.racketclash.teams

import com.olt.racketclash.database.team.DeletableTeam
import com.olt.racketclash.database.team.Sorting

data class State(
    val isLoading: Boolean = true,
    val teams: List<DeletableTeam> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(),
    val tags: Tags = Tags(),
    val sorting: Sorting = Sorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)