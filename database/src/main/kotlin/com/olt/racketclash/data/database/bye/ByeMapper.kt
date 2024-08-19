package com.olt.racketclash.data.database.bye

import com.olt.racketclash.data.Bye
import com.olt.racketclash.database.bye.SelectAll
import com.olt.racketclash.database.bye.SelectAllInRound

internal fun SelectAll.toBye() =
    Bye(
        id = id,
        roundId = roundId,
        playerId = playerId,
        playerName = name,
        playerTeamName = teamName
    )

internal fun SelectAllInRound.toBye() =
    Bye(
        id = id,
        roundId = roundId,
        playerId = playerId,
        playerName = name,
        playerTeamName = teamName
    )