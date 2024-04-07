package com.olt.racketclash.database.mapper

import com.olt.racketclash.data.Player
import com.olt.racketclash.database.SelectActive
import com.olt.racketclash.database.player.SelectAll

fun SelectAll.toPlayer() =
    Player(
        id = id,
        active = active,
        name = name,
        teamId = teamId,
        teamName = teamName,
        teamStrength = teamStrength,
        openGames = openGames,
        played = played,
        bye = bye,
        wonGames = wonGames,
        lostGames = lostGames,
        wonSets = wonSets,
        lostSets = lostSets,
        wonPoints = wonPoints,
        lostPoints = lostPoints
    )

fun SelectActive.toPlayer() =
    Player(
        id = id,
        active = active,
        name = name,
        teamId = teamId,
        teamName = teamName,
        teamStrength = teamStrength,
        openGames = openGames,
        played = played,
        bye = bye,
        wonGames = wonGames,
        lostGames = lostGames,
        wonSets = wonSets,
        lostSets = lostSets,
        wonPoints = wonPoints,
        lostPoints = lostPoints
    )