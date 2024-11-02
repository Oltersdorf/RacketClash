package com.olt.racketclash.ui.navigate

internal sealed class Screens(val name: String) {
    data object RacketClash : Screens(name = "Racket Clash")

    data object Tournaments : Screens(name = "Tournaments")

    data class AddOrUpdateTournament(
        val tournamentName: String?,
        val tournamentId: Long?
    ) : Screens(name = tournamentName ?: "New tournament")

    data class Tournament(
        val tournamentName: String,
        val tournamentId: Long
    ) : Screens(name = tournamentName)

    data class Teams(
        val tournamentId: Long
    ) : Screens(name = "Teams")

    data class AddOrUpdateTeam(
        val teamName: String?,
        val teamId: Long?,
        val tournamentId: Long
    ) : Screens(name = teamName ?: "New team")

    data class Team(
        val teamName: String,
        val teamId: Long,
        val tournamentId: Long
    ) : Screens(name = teamName)

    data class Categories(
        val tournamentId: Long
    ) : Screens(name = "Categories")

    data class AddOrUpdateCategory(
        val categoryName: String?,
        val categoryId: Long?,
        val tournamentId: Long
    ) : Screens(name = categoryName ?: "New category")

    data class Category(
        val categoryName: String,
        val categoryId: Long,
        val tournamentId: Long
    ) : Screens(name = categoryName)

    data class AddOrUpdateGames(
        val categoryId: Long,
        val tournamentId: Long
    ) : Screens(name = "Games")

    data object Players: Screens(name = "Players")

    data class Player(
        val playerName: String,
        val playerId: Long
    ) : Screens(name = playerName)

    data class AddOrUpdatePlayer(
        val playerName: String?,
        val playerId: Long?
    ) : Screens(name = playerName ?: "New player")

    data object GamRules : Screens(name = "Game rules")

    data class AddOrUpdateGameRule(
        val gameRuleName: String?,
        val gameRuleId: Long?
    ) : Screens(name = gameRuleName ?: "New game rule")
}