package com.olt.racketclash.database

import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.impl.*

class RacketClashDatabaseImpl : Database {
    override val tournaments: TournamentDatabase = TournamentDatabaseImpl()
    override val players: PlayerDatabase = PlayerDatabaseImpl()
    override val rules: RuleDatabase = RuleDatabaseImpl()
    override val teams: TeamDatabase = TeamDatabaseImpl()
    override val categories: CategoryDatabase = CategoryDatabaseImpl()
    override val schedules: ScheduleDatabase = ScheduleDatabaseImpl()
    override val games: GameDatabase = GameDatabaseImpl()
    override val teamPlayers: TeamPlayerDatabase = TeamPlayerDatabaseImpl()
    override val clubs: ClubDatabase = ClubDatabaseImpl()
    override val locations: LocationDatabase = LocationDatabaseImpl()
}