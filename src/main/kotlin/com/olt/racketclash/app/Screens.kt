package com.olt.racketclash.app

import com.olt.racketclash.data.Project

sealed class Screens {
    data object Pop : Screens()
    data class Projects(val name: String = "Projects") : Screens()
    data class NewProject(val name: String = "NewProject") : Screens()
    data class OpenProject(val name: String = "OpenProject", val project: Project) : Screens()
    data class Teams(val name: String = "Teams") : Screens()
    data class EditTeam(val name: String = "EditTeam", val teamId: Long?) : Screens()
    data class Players(val name: String = "Players") : Screens()
    data class EditPlayer(val name: String = "EditPlayer", val playerId: Long?) : Screens()
    data class Games(val name: String = "Games") : Screens()
    data class NewRound(val name: String = "NewRound") : Screens()
    data class EditRound(val name: String = "EditRound", val roundId: Long) : Screens()
    data class EditGame(val name: String = "EditGame", val roundId: Long) : Screens()
}