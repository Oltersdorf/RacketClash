package com.olt.racketclash.database.mapper

import com.olt.racketclash.data.Bye
import com.olt.racketclash.database.bye.SelectAll

fun SelectAll.toBye() =
    Bye(
        id = id,
        roundId = roundId,
        playerId = playerId,
        playerName = name,
        playerTeamName = teamName
    )