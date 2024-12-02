package com.olt.racketclash.database.adapter

import app.cash.sqldelight.ColumnAdapter
import java.time.ZoneId

internal val ZoneIdColumnAdapter = object : ColumnAdapter<ZoneId, String> {
    override fun decode(databaseValue: String): ZoneId =
        ZoneId.of(databaseValue)

    override fun encode(value: ZoneId): String =
        value.id
}