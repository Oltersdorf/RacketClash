package com.olt.racketclash.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.database.RacketClashDatabase.Companion.Schema
import com.olt.racketclash.database.player.PlayerDatabase
import com.olt.racketclash.database.player.playerAdapter
import com.olt.racketclash.database.rule.RuleDatabase
import com.olt.racketclash.database.rule.ruleAdapter
import com.olt.racketclash.database.team.TeamDatabase
import com.olt.racketclash.database.tournament.TournamentDatabase
import com.olt.racketclash.database.tournament.tournamentAdapter
import java.io.File
import java.util.*

class Database internal constructor(
    driver: SqlDriver
) {
    constructor(
        path: String
    ) : this(
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        /*driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${File(path, "Database.db").absolutePath}",
            properties = Properties().apply { put("foreign_keys", "true") }
        )*/
    )

    init {
        Schema.create(driver)
    }

    private val database = RacketClashDatabase(
        driver = driver,
        ruleAdapter = ruleAdapter(),
        playerAdapter = playerAdapter(),
        tournamentAdapter = tournamentAdapter()
    )

    val tournaments = TournamentDatabase(database = database)
    val players = PlayerDatabase(database = database)
    val rules = RuleDatabase(database = database)
    val teams = TeamDatabase(database = database)
}