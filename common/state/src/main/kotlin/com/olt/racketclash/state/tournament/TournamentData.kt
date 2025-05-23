package com.olt.racketclash.state.tournament

import com.olt.racketclash.database.api.*

data class TournamentData(
    val players: List<Player> = emptyList(),
    val teams: List<Team> = emptyList(),
    val categories: List<Category> = emptyList(),
    val games: List<Game> = emptyList(),
    val scheduledGames: List<Schedule> = emptyList(),
    val locationSuggestions: List<String> = emptyList()
)