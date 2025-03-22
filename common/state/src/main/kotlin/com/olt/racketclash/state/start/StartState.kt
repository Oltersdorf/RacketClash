package com.olt.racketclash.state.start

import com.olt.racketclash.database.table.FilteredAndOrderedPlayer
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.database.tournament.DeletableTournament

data class StartState(
    val isLoading: Boolean = true,
    val tournaments: List<DeletableTournament> = emptyList(),
    val players: List<FilteredAndOrderedPlayer> = emptyList(),
    val rules: List<FilteredAndOrderedRule> = emptyList()
)
