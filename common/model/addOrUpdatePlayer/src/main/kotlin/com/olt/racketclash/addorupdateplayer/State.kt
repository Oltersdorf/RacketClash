package com.olt.racketclash.addorupdateplayer

import com.olt.racketclash.database.player.emptyPlayer
import com.olt.racketclash.database.table.Player

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val player: Player = emptyPlayer(),
    val clubSuggestions: List<String> = emptyList()
)