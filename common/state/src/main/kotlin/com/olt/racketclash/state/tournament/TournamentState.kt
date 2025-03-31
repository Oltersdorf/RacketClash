package com.olt.racketclash.state.tournament

import com.olt.racketclash.database.api.*

data class TournamentState(
    val isLoading: Boolean = true,
    val tournament: Tournament = Tournament(),
    val players: List<Player> = emptyList(),
    val teams: List<Team> = emptyList(),
    val categories: List<Category> = emptyList(),
    val games: List<Game> = emptyList(),
    val scheduledGames: List<Schedule> = emptyList(),
    val locationSuggestions: List<String> = emptyList()
)