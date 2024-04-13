package com.olt.racketclash.ui

import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.language.translations.Language

fun Player.Sorting.text(language: Language) =
    when (this) {
        Player.Sorting.NameAscending -> language.nameAscending
        Player.Sorting.NameDescending -> language.nameDescending
        Player.Sorting.PointsAscending -> language.pointsAscending
        Player.Sorting.PointsDescending -> language.pointsDescending
        Player.Sorting.TeamAscending -> language.teamAscending
        Player.Sorting.TeamDescending -> language.teamDescending
        Player.Sorting.ByeAscending -> language.byesAscending
        Player.Sorting.ByeDescending -> language.byesDescending
        Player.Sorting.PendingAscending -> language.pendingAscending
        Player.Sorting.PendingDescending -> language.pendingDescending
        Player.Sorting.PlayedAscending -> language.playedAscending
        Player.Sorting.PlayedDescending -> language.playedDescending
    }

fun Team.Sorting.text(language: Language) =
    when (this) {
        Team.Sorting.NameAscending -> language.nameAscending
        Team.Sorting.NameDescending -> language.nameDescending
        Team.Sorting.PointsAscending -> language.pointsAscending
        Team.Sorting.PointsDescending -> language.pointsDescending
        Team.Sorting.StrengthAscending -> language.strengthAscending
        Team.Sorting.StrengthDescending -> language.strengthDescending
    }