package com.olt.racketclash.database.api

interface TeamPlayerDatabase {

    suspend fun selectPlayers(teamId: Long): Set<Long>
}