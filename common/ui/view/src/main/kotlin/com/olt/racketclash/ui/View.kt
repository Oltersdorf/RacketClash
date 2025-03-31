package com.olt.racketclash.ui

internal sealed interface View {
    data object Start : View

    data object Tournaments : View

    data class AddOrUpdateTournament(
        val tournamentName: String?,
        val tournamentId: Long?
    ) : View

    data class Tournament(
        val tournamentName: String,
        val tournamentId: Long
    ) : View

    data class Teams(
        val tournamentId: Long
    ) : View

    data class AddOrUpdateTeam(
        val teamName: String?,
        val teamId: Long?,
        val tournamentId: Long
    ) : View

    data class Team(
        val teamName: String,
        val teamId: Long,
        val tournamentId: Long
    ) : View

    data class Categories(
        val tournamentId: Long
    ) : View

    data class AddOrUpdateCategory(
        val categoryName: String?,
        val categoryId: Long?,
        val tournamentId: Long
    ) : View

    data class Category(
        val categoryName: String,
        val categoryId: Long,
        val tournamentId: Long
    ) : View

    data class AddSchedule(
        val categoryId: Long,
        val categoryName: String,
        val tournamentId: Long
    ) : View

    data class Schedule(val tournamentId: Long) : View

    data object Players: View

    data class Player(val playerId: Long) : View

    data object Rules : View

    data class Rule(val id: Long) : View
}