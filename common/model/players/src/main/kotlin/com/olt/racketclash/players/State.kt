package com.olt.racketclash.players

import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.PlayerSorting

data class State(
    val isLoading: Boolean = true,
    val players: List<Player> = emptyList(),
    val searchBarText: String = "",
    val availableTags: Tags = Tags(hasMedals = true),
    val tags: Tags = Tags(),
    val sorting: PlayerSorting = PlayerSorting.NameAsc,
    val currentPage: Int = 1,
    val lastPage: Int = 1
)