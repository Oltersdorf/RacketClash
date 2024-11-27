package com.olt.racketclash.tournaments

import com.olt.racketclash.database.tournament.DeletableTournament
import com.olt.racketclash.database.tournament.Sorting

data class State(
    val isLoading: Boolean = true,
    val tournaments: List<DeletableTournament> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(),
    val tags: Tags = Tags(),
    val sorting: Sorting = Sorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)