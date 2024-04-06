package com.olt.racketclash.data

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.olt.racketclash.database.*
import com.olt.racketclash.database.RacketClashDatabase.Companion.Schema
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
        gameTableAdapter = GameTable.Adapter(
            set1LeftAdapter = IntColumnAdapter, set1RightAdapter = IntColumnAdapter,
            set2LeftAdapter = IntColumnAdapter, set2RightAdapter = IntColumnAdapter,
            set3LeftAdapter = IntColumnAdapter, set3RightAdapter = IntColumnAdapter,
            set4LeftAdapter = IntColumnAdapter, set4RightAdapter = IntColumnAdapter,
            set5LeftAdapter = IntColumnAdapter, set5RightAdapter = IntColumnAdapter
        ),
        roundTableAdapter = RoundTable.Adapter(orderNumberAdapter = IntColumnAdapter),
        playerTableAdapter = PlayerTable.Adapter(
            openGamesAdapter = IntColumnAdapter, playedAdapter = IntColumnAdapter,
            byeAdapter = IntColumnAdapter,
            wonGamesAdapter = IntColumnAdapter, lostGamesAdapter = IntColumnAdapter,
            wonSetsAdapter = IntColumnAdapter, lostSetsAdapter = IntColumnAdapter,
            wonPointsAdapter = IntColumnAdapter, lostPointsAdapter = IntColumnAdapter
        )
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

    fun players() : Flow<List<Player>> =
        database
            .playerQueries
            .selectAll()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    Player(
                        id = selectAll.id,
                        active = selectAll.active,
                        name = selectAll.name,
                        teamId = selectAll.teamId,
                        teamName = selectAll.teamName,
                        teamStrength = selectAll.teamStrength,
                        openGames = selectAll.openGames,
                        played = selectAll.played,
                        bye = selectAll.bye,
                        wonGames = selectAll.wonGames,
                        lostGames = selectAll.lostGames,
                        wonSets = selectAll.wonSets,
                        lostSets = selectAll.lostSets,
                        wonPoints = selectAll.wonPoints,
                        lostPoints = selectAll.lostPoints
                    )
                }.sortedWith(comparator = compareBy({ it.wonGames }, { it.lostGames }, { it.wonSets }, { it.lostSets }, { it.wonPoints }, { it.lostPoints }))
            }

    fun activePlayers() : Flow<List<Player>> =
        database
            .playerQueries
            .selectActive()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    Player(
                        id = selectAll.id,
                        active = selectAll.active,
                        name = selectAll.name,
                        teamId = selectAll.teamId,
                        teamName = selectAll.teamName,
                        teamStrength = selectAll.teamStrength,
                        openGames = selectAll.openGames,
                        played = selectAll.played,
                        bye = selectAll.bye,
                        wonGames = selectAll.wonGames,
                        lostGames = selectAll.lostGames,
                        wonSets = selectAll.wonSets,
                        lostSets = selectAll.lostSets,
                        wonPoints = selectAll.wonPoints,
                        lostPoints = selectAll.lostPoints
                    )
                }
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

    fun rounds() : Flow<List<Round>> =
        database
            .roundQueries
            .selectAll()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map {
                    Round(
                        id = it.id,
                        name = it.name,
                        order = it.orderNumber
                    )
                }
            }

    fun round(id: Long) : Flow<Round?> =
        database
            .roundQueries
            .select(id = id)
            .asFlow()
            .mapToOneOrNull(context = Dispatchers.IO)
            .map {
                it?.let {
                    Round(
                        id = it.id,
                        name = it.name,
                        order = it.orderNumber
                    )
                }
            }

    fun addRound(name: String) {
        database.roundQueries.add(name = name)
    }

    fun deleteRound(id: Long) {
        database.roundQueries.delete(id = id)
    }

    fun updateRoundName(id: Long, name: String) {
        database.roundQueries.updateName(id = id, name = name)
    }

    fun games() : Flow<List<Game>> =
        database
            .gameQueries
            .selectAllNonBye()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    Game(
                        id = selectAll.id,
                        roundId = selectAll.roundId,
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
                        set1Right = selectAll.set1Right,
                        set2Left = selectAll.set2Left,
                        set2Right = selectAll.set2Right,
                        set3Left = selectAll.set3Left,
                        set3Right = selectAll.set3Right,
                        set4Left = selectAll.set4Left,
                        set4Right = selectAll.set4Right,
                        set5Left = selectAll.set5Left,
                        set5Right = selectAll.set5Right
                    )
                }
            }

    fun bye() : Flow<List<Game>> =
        database
            .gameQueries
            .selectAllBye()
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    Game(
                        id = selectAll.id,
                        roundId = selectAll.roundId,
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
                        set1Right = selectAll.set1Right,
                        set2Left = selectAll.set2Left,
                        set2Right = selectAll.set2Right,
                        set3Left = selectAll.set3Left,
                        set3Right = selectAll.set3Right,
                        set4Left = selectAll.set4Left,
                        set4Right = selectAll.set4Right,
                        set5Left = selectAll.set5Left,
                        set5Right = selectAll.set5Right
                    )
                }
            }

    fun games(roundId: Long) : Flow<List<Game>> =
        database
            .gameQueries
            .selectAllNonByeInRound(roundId = roundId)
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    Game(
                        id = selectAll.id,
                        roundId = selectAll.roundId,
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
                        set1Right = selectAll.set1Right,
                        set2Left = selectAll.set2Left,
                        set2Right = selectAll.set2Right,
                        set3Left = selectAll.set3Left,
                        set3Right = selectAll.set3Right,
                        set4Left = selectAll.set4Left,
                        set4Right = selectAll.set4Right,
                        set5Left = selectAll.set5Left,
                        set5Right = selectAll.set5Right
                    )
                }
            }

    fun bye(roundId: Long) : Flow<List<Game>> =
        database
            .gameQueries
            .selectAllByeInRound(roundId = roundId)
            .asFlow()
            .mapToList(context = Dispatchers.IO)
            .map { list ->
                list.map { selectAll ->
                    Game(
                        id = selectAll.id,
                        roundId = selectAll.roundId,
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
                        set1Right = selectAll.set1Right,
                        set2Left = selectAll.set2Left,
                        set2Right = selectAll.set2Right,
                        set3Left = selectAll.set3Left,
                        set3Right = selectAll.set3Right,
                        set4Left = selectAll.set4Left,
                        set4Right = selectAll.set4Right,
                        set5Left = selectAll.set5Left,
                        set5Right = selectAll.set5Right
                    )
                }
            }

    fun addGame(
        roundId: Long,
        playerLeft1Id: Long?,
        playerLeft2Id: Long?,
        playerRight1Id: Long?,
        playerRight2Id: Long?
    ) {
        database.transaction {
            database.gameQueries.add(
                roundId = roundId,
                playerLeft1Id = playerLeft1Id,
                playerLeft2Id = playerLeft2Id,
                playerRight1Id = playerRight1Id,
                playerRight2Id = playerRight2Id
            )

            playerLeft1Id?.let { database.playerQueries.addUndoneGame(id = it) }
            playerLeft2Id?.let { database.playerQueries.addUndoneGame(id = it) }
            playerRight1Id?.let { database.playerQueries.addUndoneGame(id = it) }
            playerRight2Id?.let { database.playerQueries.addUndoneGame(id = it) }
        }
    }

    fun updateGame(
        id: Long,
        set1Left: Int,
        set1Right: Int,
        set2Left: Int,
        set2Right: Int,
        set3Left: Int,
        set3Right: Int,
        set4Left: Int,
        set4Right: Int,
        set5Left: Int,
        set5Right: Int,
        isDone: Boolean
    ) {
        database.transaction {
            database.gameQueries.update(
                id = id,
                set1Left = set1Left,
                set1Right = set1Right,
                set2Left = set2Left,
                set2Right = set2Right,
                set3Left = set3Left,
                set3Right = set3Right,
                set4Left = set4Left,
                set4Right = set4Right,
                set5Left = set5Left,
                set5Right = set5Right,
                isDone = isDone
            )

            val game = database.gameQueries.select(id = id).executeAsOneOrNull()
            val leftWonSets = listOf(
                if (set1Left - set1Right > 0) 1 else 0,
                if (set2Left - set2Right > 0) 1 else 0,
                if (set3Left - set3Right > 0) 1 else 0,
                if (set4Left - set4Right > 0) 1 else 0,
                if (set5Left - set5Right > 0) 1 else 0
            ).sum()
            val rightWonSets = listOf(
                if (set1Left - set1Right < 0) 1 else 0,
                if (set2Left - set2Right < 0) 1 else 0,
                if (set3Left - set3Right < 0) 1 else 0,
                if (set4Left - set4Right < 0) 1 else 0,
                if (set5Left - set5Right < 0) 1 else 0
            ).sum()
            val leftWon = if (leftWonSets > rightWonSets) 1 else 0
            val rightWon = if (leftWonSets < rightWonSets) 1 else 0
            val leftPoints = set1Left + set2Left + set3Left + set4Left + set5Left
            val rightPoints = set1Right + set2Right + set3Right + set4Right + set5Right

            if (isDone) {
                game?.playerLeft1Id?.let {
                    database.playerQueries.setGameDone(
                        id = it,
                        wonGame = leftWon,
                        lostGame = rightWon,
                        wonSets = leftWonSets,
                        lostSets = rightWonSets,
                        wonPoints = leftPoints,
                        lostPoints = rightPoints
                    )
                }
                game?.playerLeft2Id?.let {
                    database.playerQueries.setGameDone(
                        id = it,
                        wonGame = leftWon,
                        lostGame = rightWon,
                        wonSets = leftWonSets,
                        lostSets = rightWonSets,
                        wonPoints = leftPoints,
                        lostPoints = rightPoints
                    )
                }
                game?.playerRight1Id?.let {
                    database.playerQueries.setGameDone(
                        id = it,
                        wonGame = rightWon,
                        lostGame = leftWon,
                        wonSets = rightWonSets,
                        lostSets = leftWonSets,
                        wonPoints = rightPoints,
                        lostPoints = leftPoints
                    )
                }
                game?.playerRight2Id?.let {
                    database.playerQueries.setGameDone(
                        id = it,
                        wonGame = rightWon,
                        lostGame = leftWon,
                        wonSets = rightWonSets,
                        lostSets = leftWonSets,
                        wonPoints = rightPoints,
                        lostPoints = leftPoints
                    )
                }
            } else {
                game?.playerLeft1Id?.let {
                    database.playerQueries.setGameUndone(
                        id = it,
                        wonGame = leftWon,
                        lostGame = rightWon,
                        wonSets = leftWonSets,
                        lostSets = rightWonSets,
                        wonPoints = leftPoints,
                        lostPoints = rightPoints
                    )
                }
                game?.playerLeft2Id?.let {
                    database.playerQueries.setGameUndone(
                        id = it,
                        wonGame = leftWon,
                        lostGame = rightWon,
                        wonSets = leftWonSets,
                        lostSets = rightWonSets,
                        wonPoints = leftPoints,
                        lostPoints = rightPoints
                    )
                }
                game?.playerRight1Id?.let {
                    database.playerQueries.setGameUndone(
                        id = it,
                        wonGame = rightWon,
                        lostGame = leftWon,
                        wonSets = rightWonSets,
                        lostSets = leftWonSets,
                        wonPoints = rightPoints,
                        lostPoints = leftPoints
                    )
                }
                game?.playerRight2Id?.let {
                    database.playerQueries.setGameUndone(
                        id = it,
                        wonGame = rightWon,
                        lostGame = leftWon,
                        wonSets = rightWonSets,
                        lostSets = leftWonSets,
                        wonPoints = rightPoints,
                        lostPoints = leftPoints
                    )
                }
            }
        }
    }

    fun deleteGame(id: Long) {
        database.transaction {
            val game = database.gameQueries.select(id = id).executeAsOneOrNull()
            database.gameQueries.delete(id = id)

            if (game != null) {
                val leftWonSets = listOf(
                    if (game.set1Left - game.set1Right > 0) 1 else 0,
                    if (game.set2Left - game.set2Right > 0) 1 else 0,
                    if (game.set3Left - game.set3Right > 0) 1 else 0,
                    if (game.set4Left - game.set4Right > 0) 1 else 0,
                    if (game.set5Left - game.set5Right > 0) 1 else 0
                ).sum()
                val rightWonSets = listOf(
                    if (game.set1Left - game.set1Right < 0) 1 else 0,
                    if (game.set2Left - game.set2Right < 0) 1 else 0,
                    if (game.set3Left - game.set3Right < 0) 1 else 0,
                    if (game.set4Left - game.set4Right < 0) 1 else 0,
                    if (game.set5Left - game.set5Right < 0) 1 else 0
                ).sum()
                val leftWon = if (leftWonSets > rightWonSets) 1 else 0
                val rightWon = if (leftWonSets < rightWonSets) 1 else 0
                val leftPoints = game.set1Left + game.set2Left + game.set3Left + game.set4Left + game.set5Left
                val rightPoints = game.set1Right + game.set2Right + game.set3Right + game.set4Right + game.set5Right

                if (game.isBye)
                    game.playerLeft1Id?.let { database.playerQueries.removeByeGame(it) }
                else if (game.isDone) {
                    game.playerLeft1Id?.let {
                        database.playerQueries.removeDoneGame(
                            id = it,
                            wonGame = leftWon,
                            lostGame = rightWon,
                            wonSets = leftWonSets,
                            lostSets = rightWonSets,
                            wonPoints = leftPoints,
                            lostPoints = rightPoints
                        )
                    }
                    game.playerLeft2Id?.let {
                        database.playerQueries.removeDoneGame(
                            id = it,
                            wonGame = leftWon,
                            lostGame = rightWon,
                            wonSets = leftWonSets,
                            lostSets = rightWonSets,
                            wonPoints = leftPoints,
                            lostPoints = rightPoints
                        )
                    }
                    game.playerRight1Id?.let {
                        database.playerQueries.removeDoneGame(
                            id = it,
                            wonGame = rightWon,
                            lostGame = leftWon,
                            wonSets = rightWonSets,
                            lostSets = leftWonSets,
                            wonPoints = rightPoints,
                            lostPoints = leftPoints
                        )
                    }
                    game.playerRight2Id?.let {
                        database.playerQueries.removeDoneGame(
                            id = it,
                            wonGame = rightWon,
                            lostGame = leftWon,
                            wonSets = rightWonSets,
                            lostSets = leftWonSets,
                            wonPoints = rightPoints,
                            lostPoints = leftPoints
                        )
                    }
                } else {
                    game.playerLeft1Id?.let { database.playerQueries.removeUndoneGame(id = it) }
                    game.playerLeft2Id?.let { database.playerQueries.removeUndoneGame(id = it) }
                    game.playerRight1Id?.let { database.playerQueries.removeUndoneGame(id = it) }
                    game.playerRight2Id?.let { database.playerQueries.removeUndoneGame(id = it) }
                }
            }

        }

    }

    fun addRoundsWithGames(
        rounds: Map<String, List<Game>>,
        bye: List<Game>
    ) {
        database.transaction {
            rounds.onEachIndexed { index, (key, value) ->
                database.roundQueries.add(name = key)
                val roundId = database.roundQueries.lastInsertRowId().executeAsOne()

                value.forEach {
                    addGame(
                        roundId = roundId,
                        playerLeft1Id = it.playerLeft1Id,
                        playerLeft2Id = it.playerLeft2Id,
                        playerRight1Id = it.playerRight1Id,
                        playerRight2Id = it.playerRight2Id
                    )
                }

                bye.filter { it.roundId == index.toLong() + 1 }.forEach { game ->
                    database.gameQueries.addBye(roundId = roundId, playerLeft1Id = game.playerLeft1Id)
                    game.playerLeft1Id?.let { database.playerQueries.addByeGame(it) }
                }
            }
        }
    }
}