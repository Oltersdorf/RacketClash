package com.olt.racketclash.data.database.round

import com.olt.racketclash.data.Round
import com.olt.racketclash.database.RoundTable

fun RoundTable.toRound() =
    Round(
        id = id,
        name = name,
        order = orderNumber
    )