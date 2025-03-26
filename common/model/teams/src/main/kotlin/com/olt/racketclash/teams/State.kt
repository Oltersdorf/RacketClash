package com.olt.racketclash.teams

import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamSorting

data class State(
    val isLoading: Boolean = true,
    val teams: List<Team> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(),
    val tags: Tags = Tags(),
    val sorting: TeamSorting = TeamSorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)