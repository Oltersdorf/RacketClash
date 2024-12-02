package com.olt.racketclash.database.game

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.Game
import com.olt.racketclash.database.adapter.ZoneIdColumnAdapter

internal fun gameAdapter() =
    Game.Adapter(
        zoneIdAdapter = ZoneIdColumnAdapter,
        categoryOrderNumberAdapter = IntColumnAdapter
    )