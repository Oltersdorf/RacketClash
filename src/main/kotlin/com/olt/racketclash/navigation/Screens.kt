package com.olt.racketclash.navigation

import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Project
import com.olt.racketclash.data.Round
import com.olt.racketclash.data.Team
import com.olt.racketclash.language.translations.Language

sealed class Screens {
    data object Pop : Screens()
    data object Projects : Screens()
    data class NewProject(val language: Language) : Screens()
    data class OpenProject(val project: Project, val language: Language) : Screens()
    data class Teams(val language: Language) : Screens()
    data class EditTeam(val team: Team?, val language: Language) : Screens()
    data class Players(val language: Language) : Screens()
    data class EditPlayer(val player: Player?, val language: Language) : Screens()
    data class Games(val language: Language) : Screens()
    data class NewRound(val language: Language) : Screens()
    data class EditRound(val round: Round, val language: Language) : Screens()
    data class EditGame(val roundId: Long, val language: Language) : Screens()
}