package com.olt.racketclash.ui

internal sealed interface View {
    data object Start : View

    data object Tournaments : View

    data class Tournament(val tournamentId: Long) : View

    data class Teams(val tournamentId: Long) : View

    data class Team(val teamId: Long) : View

    data class Categories(val tournamentId: Long) : View

    data class Category(val categoryId: Long) : View

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