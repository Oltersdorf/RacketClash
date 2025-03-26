package com.olt.racketclash.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.database.RacketClashDatabase.Companion.Schema
import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.impl.CategoryDatabaseImpl
import com.olt.racketclash.database.impl.GameDatabaseImpl
import com.olt.racketclash.database.impl.PlayerDatabaseImpl
import com.olt.racketclash.database.impl.TeamPlayerDatabaseImpl
import com.olt.racketclash.database.impl.RuleDatabaseImpl
import com.olt.racketclash.database.impl.ScheduleDatabaseImpl
import com.olt.racketclash.database.impl.TeamDatabaseImpl
import com.olt.racketclash.database.impl.TournamentDatabaseImpl
import java.io.File
import java.util.*

class RacketClashDatabaseImpl internal constructor(
    driver: SqlDriver
) : Database {
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
        driver = driver,
        ruleTableAdapter = ruleAdapter(),
        playerTableAdapter = playerAdapter(),
        tournamentAdapter = tournamentAdapter(),
        teamTableAdapter = teamAdapter(),
        categoryAdapter = categoryAdapter(),
        scheduleAdapter = scheduleAdapter(),
        gameAdapter = gameAdapter(),
        gameSetAdapter = gameSetAdapter()
    )

    override val tournaments: TournamentDatabase = TournamentDatabaseImpl(database = database)
    override val players: PlayerDatabase = PlayerDatabaseImpl(database = database)
    override val rules: RuleDatabase = RuleDatabaseImpl(database = database)
    override val teams: TeamDatabase = TeamDatabaseImpl(database = database)
    override val categories: CategoryDatabase = CategoryDatabaseImpl(database = database)
    override val schedules: ScheduleDatabase = ScheduleDatabaseImpl(database = database)
    override val games: GameDatabase = GameDatabaseImpl(database = database)
    override val teamPlayers: TeamPlayerDatabase = TeamPlayerDatabaseImpl(database = database)
}