package com.olt.racketclash.state.player

import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.Game
import com.olt.racketclash.database.api.Tournament

data class PlayerData(
    val tournaments: List<Tournament> = emptyList(),
    val categories: List<Category> = emptyList(),
    val games: List<Game> = emptyList(),
    val clubSuggestions: List<String> = emptyList()
)