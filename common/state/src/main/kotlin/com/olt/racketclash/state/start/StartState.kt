package com.olt.racketclash.state.start

import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.database.api.Tournament

data class StartState(
    val isLoading: Boolean = true,
    val tournaments: List<Tournament> = emptyList(),
    val players: List<Player> = emptyList(),
    val rules: List<Rule> = emptyList()
)
