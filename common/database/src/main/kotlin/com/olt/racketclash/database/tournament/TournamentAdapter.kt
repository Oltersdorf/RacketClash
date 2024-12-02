package com.olt.racketclash.database.tournament

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.Tournament
import com.olt.racketclash.database.adapter.ZoneIdColumnAdapter

internal fun tournamentAdapter() =
    Tournament.Adapter(
        numberOfCourtsAdapter = IntColumnAdapter,
        zoneIdAdapter = ZoneIdColumnAdapter
    )

