package com.olt.racketclash.state.team

import com.olt.racketclash.database.api.Game
import com.olt.racketclash.database.api.Player

data class TeamData(
    val players: List<Player> = emptyList(),
    val games: List<Game> = emptyList()
)