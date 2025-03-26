package com.olt.racketclash.addorupdateteam

import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.PlayerSorting
import com.olt.racketclash.database.api.Team

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val team: Team = Team(),
    val players: List<Player> = emptyList(),
    val selectedPlayers: Set<Long> = emptySet(),
    val playersLoading: Boolean = true,
    val searchBarText: String = "",
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val availableTags: Tags = Tags(hasMedals = true),
    val tags: Tags = Tags(),
    val sorting: PlayerSorting = PlayerSorting.NameAsc
)