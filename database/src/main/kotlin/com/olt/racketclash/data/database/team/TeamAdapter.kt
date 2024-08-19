package com.olt.racketclash.data.database.team

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.TeamTable

internal fun teamAdapter() = TeamTable.Adapter(strengthAdapter = IntColumnAdapter)