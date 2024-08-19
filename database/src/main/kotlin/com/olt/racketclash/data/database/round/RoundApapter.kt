package com.olt.racketclash.data.database.round

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.RoundTable

internal fun roundAdapter() = RoundTable.Adapter(orderNumberAdapter = IntColumnAdapter)