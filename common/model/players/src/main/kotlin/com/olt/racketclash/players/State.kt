package com.olt.racketclash.players

import com.olt.racketclash.database.player.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedPlayer

data class State(
    val isLoading: Boolean = true,
    val players: List<FilteredAndOrderedPlayer> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(hasMedals = true),
    val tags: Tags = Tags(),
    val sorting: Sorting = Sorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)