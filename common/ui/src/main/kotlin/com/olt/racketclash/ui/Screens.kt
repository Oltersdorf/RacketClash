package com.olt.racketclash.ui

internal sealed class Screens(val name: String) {
    data object Start : Screens(name = "Racket Clash")

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

    data class AddSchedule(
        val categoryId: Long,
        val categoryName: String,
        val tournamentId: Long
    ) : Screens(name = "Games")

    data class Schedule(
        val tournamentId: Long
    ) : Screens(name = "Schedule")

    data object Players: Screens(name = "Players")

    data class Player(
        val playerName: String,
        val playerId: Long
    ) : Screens(name = playerName)

    data class AddOrUpdatePlayer(
        val playerName: String?,
        val playerId: Long?
    ) : Screens(name = playerName ?: "New player")

    data object Rules : Screens(name = "Rules")

    data class AddOrUpdateRule(
        val ruleName: String?,
        val ruleId: Long?
    ) : Screens(name = ruleName ?: "New rule")
}