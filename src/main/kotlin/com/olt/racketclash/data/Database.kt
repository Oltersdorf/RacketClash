package com.olt.racketclash.data

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.database.GameTable
import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.RacketClashDatabase.Companion.Schema
import com.olt.racketclash.database.TeamTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.*

class Database private constructor(
    driver: SqlDriver,
    private val fileHandler: FileHandler,
    private val projectName: String
) {
    constructor(
        tournamentPath: String,
        fileHandler: FileHandler,
        projectName: String
    ) : this(
        driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${File(tournamentPath, "Database.db").absolutePath}",
            properties = Properties().apply { put("foreign_keys", "true") }
        ),
        fileHandler = fileHandler,
        projectName = projectName
    )

    private val database = RacketClashDatabase(
        driver = driver,
        teamTableAdapter = TeamTable.Adapter(strengthAdapter = IntColumnAdapter),
        gameTableAdapter = GameTable.Adapter(set1RightAdapter = IntColumnAdapter, set1LeftAdapter = IntColumnAdapter)
    )

    init {
        Schema.create(driver)
    }

    fun teams(): Flow<List<Team>> =
        database
            .teamQueries
            .selectAll()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map {
                it.map { selectAll ->
                    Team(id = selectAll.id, name = selectAll.name, strength = selectAll.strength, size = selectAll.size.toInt())
                }.sortedBy { team -> team.strength }
            }

    suspend fun addTeam(name: String, strength: Int) {
        database.teamQueries.add(name = name, strength = strength)
        fileHandler.updateTeamCountForProject(projectName = projectName, teamNumber = database.teamQueries.selectAll().executeAsList().size)
    }

    fun updateTeam(id: Long, name: String, strength: Int) {
        database.teamQueries.update(id = id, name = name, strength = strength)
    }

    suspend fun deleteTeam(id: Long) {
        database.teamQueries.delete(id = id)
        fileHandler.updateTeamCountForProject(projectName = projectName, teamNumber = database.teamQueries.selectAll().executeAsList().size)
    }

    fun player() : Flow<List<Player>> =
        database
            .playerQueries
            .selectAll()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    val isLeft = selectAll.playerLeft1Id == selectAll.id || selectAll.playerLeft2Id == selectAll.id
                    val gamesLeft = if (isLeft) {
                        if ((selectAll.set1Left?.toInt() ?: 0) > (selectAll.set1Right?.toInt() ?: 0))
                            1
                        else
                            0
                    } else {
                        if ((selectAll.set1Left?.toInt() ?: 0) > (selectAll.set1Right?.toInt() ?: 0))
                            0
                        else
                            1
                    }
                    val gamesRight = if (gamesLeft == 0) 1 else 0
                    Player(
                        id = selectAll.id,
                        active = selectAll.active,
                        name = selectAll.name,
                        teamId = selectAll.teamId,
                        teamName = selectAll.teamName,
                        played = selectAll.played?.toInt() ?: 0,
                        bye = selectAll.bye?.toInt() ?: 0,
                        games = Pair(first = gamesLeft, second = gamesRight),
                        sets = Pair(first = gamesLeft, second = gamesRight),
                        points = Pair(first = (if (isLeft) selectAll.set1Left else selectAll.set1Right)?.toInt() ?: 0, second = (if (isLeft) selectAll.set1Right else selectAll.set1Left)?.toInt() ?: 0)
                    )
                }.sortedWith(comparator = compareBy({ it.games.first }, { it.games.second }, { it.sets.first }, { it.sets.second }, { it.points.first }, { it.points.second }))
            }

    suspend fun addPlayer(name: String, teamId: Long) {
        database.playerQueries.add(name = name, teamId = teamId)
        fileHandler.updatePlayerCountForProject(projectName = projectName, playerNumber = database.playerQueries.selectAll().executeAsList().size)
    }

    fun updatePlayer(id: Long, name: String, teamId: Long) {
        database.playerQueries.update(id = id, name = name, teamId = teamId)
    }

    fun playerSetActive(id: Long, active: Boolean) {
        database.playerQueries.setActive(id = id, active = active)
    }

    suspend fun deletePlayer(id: Long) {
        database.playerQueries.delete(id = id)
        fileHandler.updatePlayerCountForProject(projectName = projectName, playerNumber = database.playerQueries.selectAll().executeAsList().size)
    }

    fun games() : Flow<List<Game>> =
        database
            .gameQueries
            .selectAll()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    Game(
                        id = selectAll.id,
                        roundName = selectAll.roundName,
                        roundNumber = selectAll.roundNumber.toInt(),
                        isDone = selectAll.isDone,
                        isBye = selectAll.isBye,
                        playerLeft1Id = selectAll.playerLeft1Id,
                        playerLeft1Name = selectAll.playerLeft1Name,
                        playerLeft2Id = selectAll.playerLeft2Id,
                        playerLeft2Name = selectAll.playerLeft2Name,
                        playerRight1Id = selectAll.playerRight1Id,
                        playerRight1Name = selectAll.playerRight1Name,
                        playerRight2Id = selectAll.playerRight2Id,
                        playerRight2Name = selectAll.playerRight2Name,
                        set1Left = selectAll.set1Left,
                        set1Right = selectAll.set1Right
                    )
                }
            }

    fun addGame(roundName: String, playerLeft1Id: Long?, playerLeft2Id: Long?, playerRight1Id: Long?, playerRight2Id: Long?, isBye: Boolean) {
        database.gameQueries.add(roundName = roundName, playerLeft1Id = playerLeft1Id, playerLeft2Id = playerLeft2Id, playerRight1Id = playerRight1Id, playerRight2Id = playerRight2Id, isBye = isBye, roundNumber = 1)
    }

    fun updateGame(id: Long, set1Left: Int, set1Right: Int, isDone: Boolean) {
        database.gameQueries.update(id = id, set1Left = set1Left, set1Right = set1Right, isDone = isDone)
    }

    fun deleteRound(roundName: String) {
        database.gameQueries.delete(roundName = roundName)
    }
}