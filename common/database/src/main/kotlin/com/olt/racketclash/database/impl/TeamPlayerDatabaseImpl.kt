package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.TeamPlayerDatabase

internal class TeamPlayerDatabaseImpl : TeamPlayerDatabase {
    private val teamPlayer = mutableListOf<Long>()

    override suspend fun selectPlayers(teamId: Long): Set<Long> =
        teamPlayer.toSet()
}