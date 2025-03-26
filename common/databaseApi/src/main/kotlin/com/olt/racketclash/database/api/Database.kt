package com.olt.racketclash.database.api

interface Database {
    val rules: RuleDatabase
    val tournaments: TournamentDatabase
    val players: PlayerDatabase
    val teamPlayers: TeamPlayerDatabase
    val games: GameDatabase
    val categories: CategoryDatabase
    val schedules: ScheduleDatabase
    val teams: TeamDatabase
}