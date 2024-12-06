package com.olt.racketclash.database.player

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.table.Player

internal fun playerAdapter() =
    Player.Adapter(birthYearAdapter = IntColumnAdapter)