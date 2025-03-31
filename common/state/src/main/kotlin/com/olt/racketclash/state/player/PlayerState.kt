package com.olt.racketclash.state.player

import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.Game
import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.Tournament

data class PlayerState(
    val isLoading: Boolean = true,
    val player: Player = Player(),
    val tournaments: List<Tournament> = emptyList(),
    val categories: List<Category> = emptyList(),
    val games: List<Game> = emptyList(),
    val clubSuggestions: List<String> = emptyList()
)