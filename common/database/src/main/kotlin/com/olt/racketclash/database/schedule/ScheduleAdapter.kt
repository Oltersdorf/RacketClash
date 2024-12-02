package com.olt.racketclash.database.schedule

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.Schedule
import com.olt.racketclash.database.adapter.ZoneIdColumnAdapter

internal fun scheduleAdapter() =
    Schedule.Adapter(
        zoneIdAdapter = ZoneIdColumnAdapter,
        categoryOrderNumberAdapter = IntColumnAdapter
    )