package com.olt.racketclash.database.tournament

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.adapter.ZoneIdColumnAdapter
import com.olt.racketclash.database.table.Tournament

internal fun tournamentAdapter() =
    Tournament.Adapter(
        numberOfCourtsAdapter = IntColumnAdapter,
        zoneIdAdapter = ZoneIdColumnAdapter
    )

