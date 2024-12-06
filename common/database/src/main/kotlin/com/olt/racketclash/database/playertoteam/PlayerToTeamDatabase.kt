package com.olt.racketclash.database.playertoteam

import com.olt.racketclash.database.RacketClashDatabase

class PlayerToTeamDatabase(private val database: RacketClashDatabase) {

    fun playerIn(teamId: Long): Set<Long> =
        database.playerToTeamQueries.playerIn(teamId = teamId).executeAsList().toSet()
}