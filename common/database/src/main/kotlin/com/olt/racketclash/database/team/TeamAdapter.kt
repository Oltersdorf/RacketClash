package com.olt.racketclash.database.team

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.table.TeamTable

internal fun teamAdapter() =
    TeamTable.Adapter(rankAdapter = IntColumnAdapter)