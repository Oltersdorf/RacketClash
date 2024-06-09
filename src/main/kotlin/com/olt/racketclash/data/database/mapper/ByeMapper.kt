package com.olt.racketclash.data.database.mapper

import com.olt.racketclash.data.Bye
import com.olt.racketclash.database.bye.SelectAll
import com.olt.racketclash.database.bye.SelectAllInRound

fun SelectAll.toBye() =
    Bye(
        id = id,
        roundId = roundId,
        playerId = playerId,
        playerName = name,
        playerTeamName = teamName
    )

fun SelectAllInRound.toBye() =
    Bye(
        id = id,
        roundId = roundId,
        playerId = playerId,
        playerName = name,
        playerTeamName = teamName
    )