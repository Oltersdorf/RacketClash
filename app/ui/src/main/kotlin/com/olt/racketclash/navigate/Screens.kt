package com.olt.racketclash.navigate

sealed class Screens {
    data object Pop : Screens()
    data class Projects(val name: String = "Projects") : Screens()
    data class NewProject(val name: String = "NewProject") : Screens()
    data class Teams(val name: String = "Teams", val projectId: Long) : Screens()
    data class EditTeam(val name: String = "EditTeam", val teamId: Long?, val projectId: Long) : Screens()
    data class Players(val name: String = "Players", val projectId: Long) : Screens()
    data class EditPlayer(val name: String = "EditPlayer", val playerId: Long?, val projectId: Long) : Screens()
    data class Games(val name: String = "Games", val projectId: Long) : Screens()
    data class NewRound(val name: String = "NewRound", val projectId: Long) : Screens()
    data class EditRound(val name: String = "EditRound", val roundId: Long, val projectId: Long) : Screens()
    data class EditGame(val name: String = "EditGame", val roundId: Long, val projectId: Long) : Screens()
}