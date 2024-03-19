package com.olt.racketclash.navigation

import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Round
import com.olt.racketclash.data.Team

sealed class Screens {
    data object Pop : Screens()
    data object Projects : Screens()
    data object NewProject : Screens()
    data class OpenProject(val projectLocation: String, val projectName: String) : Screens()
    data object Teams : Screens()
    data class EditTeam(val team: Team?) : Screens()
    data object Players : Screens()
    data class EditPlayer(val player: Player?) : Screens()
    data object Games : Screens()
    data object NewRound : Screens()
    data class EditRound(val round: Round) : Screens()
}