package com.olt.racketclash.database.team

import com.olt.racketclash.database.table.Team

fun emptyTeam() =
    Team(
        id = 0L,
        name = "",
        rank = 1,
        tournamentId = 0L,
        size = 0,
        gamesWon = 0L,
        gamesDraw = 0L,
        gamesLost = 0L,
        gamePointsWon = 0L,
        gamePointsLost = 0L
    )