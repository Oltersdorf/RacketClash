package com.olt.racketclash.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.app.RacketClashModel
import com.olt.racketclash.data.*
import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.RacketClashDatabase.Companion.Schema
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.*

class Database private constructor(
    driver: SqlDriver,
    private val appModel: RacketClashModel
) {
    constructor(
        tournamentPath: String,
        appModel: RacketClashModel
    ) : this(
        driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${File(tournamentPath, "Database.db").absolutePath}",
            properties = Properties().apply { put("foreign_keys", "true") }
        ),
        appModel = appModel
    )

    init {
        Schema.create(driver)
    }

    private val database = RacketClashDatabase(
        driver = driver,
        teamTableAdapter = TeamsDatabase.teamAdapter,
        gameTableAdapter = GamesDatabase.gameAdapter,
        roundTableAdapter = RoundsDatabase.roundAdapter
    )

    private val teamsDatabase = TeamsDatabase(queries = database.teamQueries)
    private val playersDatabase = PlayersDatabase(queries = database.playerQueries)
    private val roundsDatabase = RoundsDatabase(queries = database.roundQueries)
    private val gamesDatabase = GamesDatabase(queries = database.gameQueries)
    private val byeDatabase = ByeDatabase(queries = database.byeQueries)

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

    //team functions
    fun addTeam(name: String, strength: Int) {
        val size = database.transactionWithResult {
            teamsDatabase.addTeam(name = name, strength = strength)
        }
        appModel.updateTeamCount(teamNumber = size)
    }

    fun updateTeam(id: Long, name: String, strength: Int) =
        teamsDatabase.updateTeam(id = id, name = name, strength = strength)

    fun deleteTeam(id: Long) {
        val size = database.transactionWithResult {
            teamsDatabase.deleteTeam(id = id)
        }
        appModel.updateTeamCount(teamNumber = size)
    }


    //player functions
    fun addPlayer(name: String, teamId: Long) {
        val size = database.transactionWithResult {
            playersDatabase.addPlayer(name = name, teamId = teamId)
        }
        appModel.updatePlayerCount(playerNumber = size)
    }

    fun updatePlayer(id: Long, name: String, teamId: Long) =
        playersDatabase.updatePlayer(id = id, name = name, teamId = teamId)

    fun playerSetActive(id: Long, active: Boolean) =
        playersDatabase.setActive(id = id, active = active)

    fun deletePlayer(id: Long) {
        val size = database.transactionWithResult {
            playersDatabase.deletePlayer(id = id)
        }
        appModel.updatePlayerCount(playerNumber = size)
    }

    //round functions
    fun addRound(name: String) =
        roundsDatabase.addRound(name = name)

    fun deleteRound(id: Long) =
        roundsDatabase.deleteRound(id = id)

    fun updateRoundName(id: Long, name: String) =
        roundsDatabase.updateRoundName(id = id, name = name)

    //game functions
    fun addGame(
        roundId: Long,
        playerLeft1Id: Long?, playerLeft2Id: Long?,
        playerRight1Id: Long?, playerRight2Id: Long?
    ) {
        database.transaction {
            gamesDatabase.addGame(
                roundId = roundId,
                playerLeft1Id = playerLeft1Id, playerLeft2Id = playerLeft2Id,
                playerRight1Id = playerRight1Id, playerRight2Id = playerRight2Id
            )
        }
    }

    fun updateGame(
        id: Long, isDone: Boolean,
        set1Left: Int, set1Right: Int,
        set2Left: Int, set2Right: Int,
        set3Left: Int, set3Right: Int,
        set4Left: Int, set4Right: Int,
        set5Left: Int, set5Right: Int
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
        }
    }

    fun deleteGame(id: Long) {
        database.transaction {
            gamesDatabase.deleteGame(id = id)
        }
    }

    fun deleteBye(id: Long) {
        database.transaction {
            byeDatabase.delete(id = id)
        }
    }

    fun addBye(roundId: Long, playerId: Long?) {
        playerId?.let { database.byeQueries.add(roundId = roundId, playerId = it) }
    }

    fun addRoundsWithGames(
        rounds: Map<String, List<Game>>,
        bye: List<Bye>
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
        }
    }
}