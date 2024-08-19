package com.olt.racketclash.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.data.database.bye.ByeDatabase
import com.olt.racketclash.data.database.game.GameDatabase
import com.olt.racketclash.data.database.game.gameAdapter
import com.olt.racketclash.data.database.player.PlayerDatabase
import com.olt.racketclash.data.database.project.ProjectDatabase
import com.olt.racketclash.data.database.project.projectAdapter
import com.olt.racketclash.data.database.round.RoundDatabase
import com.olt.racketclash.data.database.round.roundAdapter
import com.olt.racketclash.data.database.team.TeamDatabase
import com.olt.racketclash.data.database.team.teamAdapter
import com.olt.racketclash.database.RacketClashDatabase
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
        driver = driver,
        projectTableAdapter = projectAdapter(),
        teamTableAdapter = teamAdapter(),
        gameTableAdapter = gameAdapter(),
        roundTableAdapter = roundAdapter()
    )

    val projectDatabase: IProjectDatabase = ProjectDatabase(database = database)
    val teamDatabase: ITeamDatabase = TeamDatabase(database = database)
    val playerDatabase: IPlayerDatabase = PlayerDatabase(database = database)
    val roundDatabase: IRoundDatabase = RoundDatabase(database = database)
    val gameDatabase: IGameDatabase = GameDatabase(database = database)
    val byeDatabase: IByeDatabase = ByeDatabase(database = database)
}