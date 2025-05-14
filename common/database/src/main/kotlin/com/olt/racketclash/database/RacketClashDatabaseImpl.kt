package com.olt.racketclash.database

import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.impl.CategoryDatabaseImpl
import com.olt.racketclash.database.impl.GameDatabaseImpl
import com.olt.racketclash.database.impl.PlayerDatabaseImpl
import com.olt.racketclash.database.impl.TeamPlayerDatabaseImpl
import com.olt.racketclash.database.impl.RuleDatabaseImpl
import com.olt.racketclash.database.impl.ScheduleDatabaseImpl
import com.olt.racketclash.database.impl.TeamDatabaseImpl
import com.olt.racketclash.database.impl.TournamentDatabaseImpl

class RacketClashDatabaseImpl : Database {
    override val tournaments: TournamentDatabase = TournamentDatabaseImpl()
    override val players: PlayerDatabase = PlayerDatabaseImpl()
    override val rules: RuleDatabase = RuleDatabaseImpl()
    override val teams: TeamDatabase = TeamDatabaseImpl()
    override val categories: CategoryDatabase = CategoryDatabaseImpl()
    override val schedules: ScheduleDatabase = ScheduleDatabaseImpl()
    override val games: GameDatabase = GameDatabaseImpl()
    override val teamPlayers: TeamPlayerDatabase = TeamPlayerDatabaseImpl()
}