package com.olt.racketclash.database.mapper

import com.olt.racketclash.data.Team
import com.olt.racketclash.database.team.SelectAll

fun SelectAll.toTeam() =
    Team(
        id = id,
        name = name,
        strength = strength,
        size = size.toInt(),
        openGames = openGames.toInt(),
        bye = byes.toInt(),
        played = played.toInt(),
        wonGames = wonGames?.toInt() ?: 0,
        lostGames = lostGames?.toInt() ?: 0,
        wonSets = wonSets?.toInt() ?: 0,
        lostSets = lostSets?.toInt() ?: 0,
        wonPoints = wonPoints?.toInt() ?: 0,
        lostPoints = lostPoints?.toInt() ?: 0
    )