package com.olt.racketclash.tournaments

import com.olt.racketclash.database.api.Tournament
import com.olt.racketclash.database.api.TournamentSorting

data class State(
    val isLoading: Boolean = true,
    val tournaments: List<Tournament> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(),
    val tags: Tags = Tags(),
    val sorting: TournamentSorting = TournamentSorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)