package com.olt.racketclash.database.set

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.table.GameSet

internal fun gameSetAdapter() =
    GameSet.Adapter(
        orderNumberAdapter = IntColumnAdapter,
        leftPointsAdapter = IntColumnAdapter,
        rightPointsAdapter = IntColumnAdapter
    )