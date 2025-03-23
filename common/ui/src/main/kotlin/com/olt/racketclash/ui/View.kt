package com.olt.racketclash.ui

internal sealed class View(val name: String) {
    data object Start : View(name = "Racket Clash")

    data object Tournaments : View(name = "Tournaments")

    data class AddOrUpdateTournament(
        val tournamentName: String?,
        val tournamentId: Long?
    ) : View(name = tournamentName ?: "New tournament")

    data class Tournament(
        val tournamentName: String,
        val tournamentId: Long
    ) : View(name = tournamentName)

    data class Teams(
        val tournamentId: Long
    ) : View(name = "Teams")

    data class AddOrUpdateTeam(
        val teamName: String?,
        val teamId: Long?,
        val tournamentId: Long
    ) : View(name = teamName ?: "New team")

    data class Team(
        val teamName: String,
        val teamId: Long,
        val tournamentId: Long
    ) : View(name = teamName)

    data class Categories(
        val tournamentId: Long
    ) : View(name = "Categories")

    data class AddOrUpdateCategory(
        val categoryName: String?,
        val categoryId: Long?,
        val tournamentId: Long
    ) : View(name = categoryName ?: "New category")

    data class Category(
        val categoryName: String,
        val categoryId: Long,
        val tournamentId: Long
    ) : View(name = categoryName)

    data class AddSchedule(
        val categoryId: Long,
        val categoryName: String,
        val tournamentId: Long
    ) : View(name = "Games")

    data class Schedule(
        val tournamentId: Long
    ) : View(name = "Schedule")

    data object Players: View(name = "Players")

    data class Player(
        val playerName: String,
        val playerId: Long
    ) : View(name = playerName)

    data class AddOrUpdatePlayer(
        val playerName: String?,
        val playerId: Long?
    ) : View(name = playerName ?: "New player")

    data object Rules : View(name = "Rules")

    data class Rule(val id: Long) : View("Rule")
}