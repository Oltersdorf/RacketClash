package com.olt.racketclash.database.rule

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.Rule

internal fun ruleAdapter() =
    Rule.Adapter(
        maxSetsAdapter = IntColumnAdapter,
        winSetsAdapter = IntColumnAdapter,
        maxPointsAdapter = IntColumnAdapter,
        winPointsAdapter = IntColumnAdapter,
        pointsDifferenceAdapter = IntColumnAdapter,
        gamePointsForWinAdapter = IntColumnAdapter,
        gamePointsForLoseAdapter = IntColumnAdapter,
        gamePointsForDrawAdapter = IntColumnAdapter,
        gamePointsForRestAdapter = IntColumnAdapter,
        setPointsForRestAdapter = IntColumnAdapter,
        pointPointsForRestAdapter = IntColumnAdapter
    )