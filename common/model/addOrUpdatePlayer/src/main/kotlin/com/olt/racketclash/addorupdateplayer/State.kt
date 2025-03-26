package com.olt.racketclash.addorupdateplayer

import com.olt.racketclash.database.api.Player

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val player: Player = Player(),
    val clubSuggestions: List<String> = emptyList()
)