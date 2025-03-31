package com.olt.racketclash.state.team

import com.olt.racketclash.database.api.Game
import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.Team

data class TeamState(
    val isLoading: Boolean = true,
    val team: Team = Team(),
    val players: List<Player> = emptyList(),
    val games: List<Game> = emptyList()
)