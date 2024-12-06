package com.olt.racketclash.database.game

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.adapter.ZoneIdColumnAdapter
import com.olt.racketclash.database.table.Game

internal fun gameAdapter() =
    Game.Adapter(
        zoneIdAdapter = ZoneIdColumnAdapter,
        categoryOrderNumberAdapter = IntColumnAdapter
    )