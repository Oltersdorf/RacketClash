package com.olt.racketclash.data.database.player

import com.olt.racketclash.data.Player
import com.olt.racketclash.database.SelectActive
import com.olt.racketclash.database.player.Select
import com.olt.racketclash.database.player.SelectAll

internal fun SelectAll.toPlayer() =
    Player(
        id = id,
        active = active,
        name = name,
        teamId = teamId,
        teamName = teamName,
        teamStrength = teamStrength,
        openGames = openGames.toInt(),
        played = played.toInt(),
        bye = bye.toInt(),
        wonGames = wonGames?.toInt() ?: 0,
        lostGames = lostGames?.toInt() ?: 0,
        wonSets = wonSets?.toInt() ?: 0,
        lostSets = lostSets?.toInt() ?: 0,
        wonPoints = wonPoints?.toInt() ?: 0,
        lostPoints = lostPoints?.toInt() ?: 0
    )

internal fun Select.toPlayer() =
    Player(
        id = id,
        active = active,
        name = name,
        teamId = teamId,
        teamName = teamName,
        teamStrength = teamStrength,
        openGames = openGames.toInt(),
        played = played.toInt(),
        bye = bye.toInt(),
        wonGames = wonGames?.toInt() ?: 0,
        lostGames = lostGames?.toInt() ?: 0,
        wonSets = wonSets?.toInt() ?: 0,
        lostSets = lostSets?.toInt() ?: 0,
        wonPoints = wonPoints?.toInt() ?: 0,
        lostPoints = lostPoints?.toInt() ?: 0
    )

internal fun SelectActive.toPlayer() =
    Player(
        id = id,
        active = true,
        name = name,
        teamId = teamId,
        teamName = teamName,
        teamStrength = teamStrength,
        openGames = openGames.toInt(),
        played = played.toInt(),
        bye = bye.toInt(),
        wonGames = wonGames?.toInt() ?: 0,
        lostGames = lostGames?.toInt() ?: 0,
        wonSets = wonSets?.toInt() ?: 0,
        lostSets = lostSets?.toInt() ?: 0,
        wonPoints = wonPoints?.toInt() ?: 0,
        lostPoints = lostPoints?.toInt() ?: 0
    )