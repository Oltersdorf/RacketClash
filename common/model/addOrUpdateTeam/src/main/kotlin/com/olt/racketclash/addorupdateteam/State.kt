package com.olt.racketclash.addorupdateteam

import com.olt.racketclash.database.player.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedPlayer
import com.olt.racketclash.database.table.Team
import com.olt.racketclash.database.team.emptyTeam

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val team: Team = emptyTeam(),
    val players: List<FilteredAndOrderedPlayer> = emptyList(),
    val selectedPlayers: Set<Long> = emptySet(),
    val playersLoading: Boolean = true,
    val searchBarText: String = "",
    val currentPage: Int = 1,
    val lastPage: Int = 1,
    val availableTags: Tags = Tags(hasMedals = true),
    val tags: Tags = Tags(),
    val sorting: Sorting = Sorting.NameAsc
)