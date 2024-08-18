package com.olt.racketclash.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.data.*
import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.RacketClashDatabase.Companion.Schema
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.*

class Database private constructor(
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
        projectTableAdapter = ProjectsDatabase.projectAdapter,
        teamTableAdapter = TeamsDatabase.teamAdapter,
        gameTableAdapter = GamesDatabase.gameAdapter,
        roundTableAdapter = RoundsDatabase.roundAdapter
    )

    private val projectsDatabase = ProjectsDatabase(queries = database.projectQueries)
    private val teamsDatabase = TeamsDatabase(queries = database.teamQueries)
    private val playersDatabase = PlayersDatabase(queries = database.playerQueries)
    private val roundsDatabase = RoundsDatabase(queries = database.roundQueries)
    private val gamesDatabase = GamesDatabase(queries = database.gameQueries)
    private val byeDatabase = ByeDatabase(queries = database.byeQueries)

    fun projects(): Flow<List<Project>> = projectsDatabase.projects()
    fun projectSettings(id: Long): Flow<ProjectSettings?> = projectsDatabase.projectSettings(id = id)
    fun teams(): Flow<List<Team>> = teamsDatabase.teams()
    fun team(id: Long): Flow<Team?> = teamsDatabase.team(id = id)
    fun players(): Flow<List<Player>> = playersDatabase.players()
    fun player(id: Long): Flow<Player?> = playersDatabase.player(id = id)
    fun activePlayers(): Flow<List<Player>> = playersDatabase.activePlayers()
    fun rounds(): Flow<List<Round>> = roundsDatabase.rounds()
    fun round(id: Long) : Flow<Round?> = roundsDatabase.round(id = id)
    fun games() : Flow<List<Game>> = gamesDatabase.games()
    fun bye() : Flow<List<Bye>> = byeDatabase.byes()
    fun games(roundId: Long) : Flow<List<Game>> = gamesDatabase.games(roundId = roundId)
    fun bye(roundId: Long) : Flow<List<Bye>> = byeDatabase.byes(roundId = roundId)

    //project functions
    fun addProject(name: String) =
        projectsDatabase.addProject(name = name)

    fun deleteProject(id: Long) =
        projectsDatabase.deleteProject(id = id)

    fun updateFields(id: Long, fields: Int) =
        projectsDatabase.updateFields(id = id, fields = fields)

    fun updateTimeout(id: Long, timeout: Int) =
        projectsDatabase.updateTimeout(id = id, timeout = timeout)

    fun updateGamePointsForBye(id: Long, gamePointsForBye: Int) =
        projectsDatabase.updateGamePointsForBye(id = id, gamePointsForBye = gamePointsForBye)

    fun updateSetPointsForBye(id: Long, setPointsForBye: Int) =
        projectsDatabase.updateSetPointsForBye(id = id, setPointsForBye = setPointsForBye)

    fun updatePointsForBye(id: Long, pointsForBye: Int) =
        projectsDatabase.updatePointsForBye(id = id, pointsForBye = pointsForBye)

    //team functions
    fun addTeam(name: String, strength: Int, projectId: Long) =
        database.transaction {
            teamsDatabase.addTeam(name = name, strength = strength, projectId = projectId)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun updateTeam(id: Long, name: String, strength: Int, projectId: Long) =
        database.transaction {
            teamsDatabase.updateTeam(id = id, name = name, strength = strength)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun deleteTeam(id: Long, projectId: Long) =
        database.transaction {
            teamsDatabase.deleteTeam(id = id)
            projectsDatabase.updateLastModified(id = projectId)
        }

    //player functions
    fun addPlayer(name: String, teamId: Long, projectId: Long) =
        database.transaction {
            playersDatabase.addPlayer(name = name, teamId = teamId, projectId = projectId)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun updatePlayer(id: Long, name: String, teamId: Long, projectId: Long) =
        database.transaction {
            playersDatabase.updatePlayer(id = id, name = name, teamId = teamId)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun playerSetActive(id: Long, active: Boolean, projectId: Long) =
        database.transaction {
            playersDatabase.setActive(id = id, active = active)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun deletePlayer(id: Long, projectId: Long) =
        database.transaction {
            playersDatabase.deletePlayer(id = id)
            projectsDatabase.updateLastModified(id = projectId)
        }

    //round functions
    fun addRound(name: String, projectId: Long) =
        database.transaction {
            roundsDatabase.addRound(name = name)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun deleteRound(id: Long, projectId: Long) =
        database.transaction {
            roundsDatabase.deleteRound(id = id)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun updateRoundName(id: Long, name: String, projectId: Long) =
        database.transaction {
            roundsDatabase.updateRoundName(id = id, name = name)
            projectsDatabase.updateLastModified(id = projectId)
        }

    //game functions
    fun addGame(
        roundId: Long,
        playerLeft1Id: Long?, playerLeft2Id: Long?,
        playerRight1Id: Long?, playerRight2Id: Long?,
        projectId: Long
    ) {
        database.transaction {
            gamesDatabase.addGame(
                roundId = roundId,
                playerLeft1Id = playerLeft1Id, playerLeft2Id = playerLeft2Id,
                playerRight1Id = playerRight1Id, playerRight2Id = playerRight2Id
            )
            projectsDatabase.updateLastModified(id = projectId)
        }
    }

    fun updateGame(
        id: Long, isDone: Boolean,
        set1Left: Int, set1Right: Int,
        set2Left: Int, set2Right: Int,
        set3Left: Int, set3Right: Int,
        set4Left: Int, set4Right: Int,
        set5Left: Int, set5Right: Int,
        projectId: Long
    ) {
        database.transaction {
            gamesDatabase.updateGame(
                id = id, isDone = isDone,
                set1Left = set1Left, set1Right = set1Right,
                set2Left = set2Left, set2Right = set2Right,
                set3Left = set3Left, set3Right = set3Right,
                set4Left = set4Left, set4Right = set4Right,
                set5Left = set5Left, set5Right = set5Right
            )

            val game = gamesDatabase.select(id = id)

            if (isDone && game != null) {
                val lastPlayed = System.currentTimeMillis()
                playersDatabase.setLastPlayed(id = game.playerLeft1Id, lastPlayed = lastPlayed)
                playersDatabase.setLastPlayed(id = game.playerLeft2Id, lastPlayed = lastPlayed)
                playersDatabase.setLastPlayed(id = game.playerRight1Id, lastPlayed = lastPlayed)
                playersDatabase.setLastPlayed(id = game.playerRight2Id, lastPlayed = lastPlayed)
            } else if (game != null) {
                playersDatabase.setLastPlayed(id = game.playerLeft1Id, lastPlayed = null)
                playersDatabase.setLastPlayed(id = game.playerLeft2Id, lastPlayed = null)
                playersDatabase.setLastPlayed(id = game.playerRight1Id, lastPlayed = null)
                playersDatabase.setLastPlayed(id = game.playerRight2Id, lastPlayed = null)
            }

            projectsDatabase.updateLastModified(id = projectId)
        }
    }

    fun deleteGame(id: Long, projectId: Long) =
        database.transaction {
            gamesDatabase.deleteGame(id = id)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun deleteBye(id: Long, projectId: Long) =
        database.transaction {
            byeDatabase.delete(id = id)
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun addBye(roundId: Long, playerId: Long?, projectId: Long) =
        database.transaction {
            playerId?.let { database.byeQueries.add(roundId = roundId, playerId = it) }
            projectsDatabase.updateLastModified(id = projectId)
        }

    fun addRoundsWithGames(
        rounds: Map<String, List<Game>>,
        bye: List<Bye>,
        projectId: Long
    ) {
        database.transaction {
            rounds.onEachIndexed { index, (key, value) ->
                roundsDatabase.addRound(name = key)
                val roundId = roundsDatabase.lastInsertedRow()

                value.forEach { game ->
                    gamesDatabase.addGame(
                        roundId = roundId,
                        playerLeft1Id = game.playerLeft1Id, playerLeft2Id = game.playerLeft2Id,
                        playerRight1Id = game.playerRight1Id, playerRight2Id = game.playerRight2Id
                    )
                }

                bye.filter { it.roundId == index.toLong() + 1 }.forEach { bye ->
                    byeDatabase.add(roundId = roundId, playerId = bye.playerId)
                }
            }

            projectsDatabase.updateLastModified(id = projectId)
        }
    }
}