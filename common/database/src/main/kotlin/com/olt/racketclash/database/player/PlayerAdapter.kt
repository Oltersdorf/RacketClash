package com.olt.racketclash.database.player

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.table.PlayerTable

internal fun playerAdapter() =
    PlayerTable.Adapter(birthYearAdapter = IntColumnAdapter)