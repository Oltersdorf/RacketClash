package com.olt.racketclash.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.database.RacketClashDatabase.Companion.Schema
import java.io.File
import java.util.*

class Database internal constructor(
    driver: SqlDriver
) {
    constructor(
        path: String
    ) : this(
        driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${File(path, "Database.db").absolutePath}",
            properties = Properties().apply { put("foreign_keys", "true") }
        )
    )

    init {
        Schema.create(driver)
    }

    private val database = RacketClashDatabase(
        driver = driver
    )
}