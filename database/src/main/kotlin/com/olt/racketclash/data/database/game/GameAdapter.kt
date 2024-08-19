package com.olt.racketclash.data.database.game

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.GameTable

internal fun gameAdapter() = GameTable.Adapter(
    set1LeftAdapter = IntColumnAdapter, set1RightAdapter = IntColumnAdapter,
    set2LeftAdapter = IntColumnAdapter, set2RightAdapter = IntColumnAdapter,
    set3LeftAdapter = IntColumnAdapter, set3RightAdapter = IntColumnAdapter,
    set4LeftAdapter = IntColumnAdapter, set4RightAdapter = IntColumnAdapter,
    set5LeftAdapter = IntColumnAdapter, set5RightAdapter = IntColumnAdapter,
    pointsLeftAdapter = IntColumnAdapter, pointsRightAdapter = IntColumnAdapter,
    setsLeftWonAdapter = IntColumnAdapter, setsRightWonAdapter = IntColumnAdapter
)