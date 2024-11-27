package com.olt.racketclash.database.tournament

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.Tournament
import java.time.ZoneId

internal fun tournamentAdapter() =
    Tournament.Adapter(
        numberOfCourtsAdapter = IntColumnAdapter,
        zoneIdAdapter = ZoneIdColumnAdapter
    )

private val ZoneIdColumnAdapter = object : ColumnAdapter<ZoneId, String> {
    override fun decode(databaseValue: String): ZoneId =
        ZoneId.of(databaseValue)

    override fun encode(value: ZoneId): String =
        value.id
}