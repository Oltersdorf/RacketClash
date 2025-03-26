package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.TeamPlayerDatabase

internal class TeamPlayerDatabaseImpl(
    private val database: RacketClashDatabase
) : TeamPlayerDatabase {

    override suspend fun selectPlayers(teamId: Long): Set<Long> =
        database
            .playerToTeamQueries
            .playerIn(teamId = teamId)
            .executeAsList()
            .toSet()
}