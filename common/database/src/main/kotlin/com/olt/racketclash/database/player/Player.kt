package com.olt.racketclash.database.player

import com.olt.racketclash.database.table.Player

fun emptyPlayer() =
    Player(
        id = 0L,
        name = "",
        birthYear = 1900,
        club = "",
        numberOfTournaments = 0,
        goldMedals = 0,
        silverMedals = 0,
        bronzeMedals = 0,
        gamesPlayed = 0,
        gamesScheduled = 0
    )