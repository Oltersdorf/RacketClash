package com.olt.racketclash.state.player

import com.olt.racketclash.database.api.Player

data class PlayerState(
    val isLoading: Boolean = true,
    val clubSuggestions: List<String> = emptyList(),
    val player: Player = Player()
)