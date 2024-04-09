package com.olt.racketclash.ui

import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team

fun Player.Sorting.text() =
    when (this) {
        Player.Sorting.NameAscending -> "Name ascending"
        Player.Sorting.NameDescending -> "Name descending"
        Player.Sorting.PointsAscending -> "Points ascending"
        Player.Sorting.PointsDescending -> "Points descending"
        Player.Sorting.TeamAscending -> "Team ascending"
        Player.Sorting.TeamDescending -> "Team descending"
        Player.Sorting.ByeAscending -> "Bye ascending"
        Player.Sorting.ByeDescending -> "Bye descending"
        Player.Sorting.PendingAscending -> "Pending ascending"
        Player.Sorting.PendingDescending -> "Pending descending"
        Player.Sorting.PlayedAscending -> "Played ascending"
        Player.Sorting.PlayedDescending -> "Played descending"
    }

fun Team.Sorting.text() =
    when (this) {
        Team.Sorting.NameAscending -> "Name ascending"
        Team.Sorting.NameDescending -> "Name descending"
        Team.Sorting.PointsAscending -> "Points ascending"
        Team.Sorting.PointsDescending -> "Points descending"
        Team.Sorting.StrengthAscending -> "Strength ascending"
        Team.Sorting.StrengthDescending -> "Strength descending"
    }