package com.olt.racketclash.ui.navigate

import com.olt.racketclash.database.Database

internal sealed class Screens(val name: String) {
    data object RacketClash : Screens(name = "Racket Clash")

    data class Tournaments(
        val database: Database
    ) : Screens(name = "Tournaments")

    data class AddOrUpdateTournament(
        val database: Database,
        val tournamentName: String?,
        val tournamentId: Long?
    ) : Screens(name = tournamentName ?: "New tournament")

    data class Tournament(
        val database: Database,
        val tournamentName: String,
        val tournamentId: Long
    ) : Screens(name = tournamentName)

    data class Teams(
        val database: Database,
        val tournamentId: Long
    ) : Screens(name = "Teams")

    data class AddOrUpdateTeam(
        val database: Database,
        val teamName: String?,
        val teamId: Long?,
        val tournamentId: Long
    ) : Screens(name = teamName ?: "New team")

    data class Team(
        val database: Database,
        val teamName: String,
        val teamId: Long,
        val tournamentId: Long
    ) : Screens(name = teamName)

    data class Categories(
        val database: Database,
        val tournamentId: Long
    ) : Screens(name = "Categories")

    data class AddOrUpdateCategory(
        val database: Database,
        val categoryName: String?,
        val categoryId: Long?,
        val tournamentId: Long
    ) : Screens(name = categoryName ?: "New category")

    data class Category(
        val database: Database,
        val categoryName: String,
        val categoryId: Long,
        val tournamentId: Long
    ) : Screens(name = categoryName)

    data class AddOrUpdateGames(
        val database: Database,
        val categoryId: Long,
        val tournamentId: Long
    ) : Screens(name = "Games")

    data class Players(
        val database: Database
    ) : Screens(name = "Players")

    data class AddOrUpdatePlayer(
        val database: Database,
        val playerName: String?,
        val playerId: Long?
    ) : Screens(name = playerName ?: "New player")

    data class GamRules(
        val database: Database
    ) : Screens(name = "Game rules")

    data class AddOrUpdateGameRule(
        val database: Database,
        val gameRuleName: String?,
        val gameRuleId: Long?
    ) : Screens(name = gameRuleName ?: "New game rule")
}