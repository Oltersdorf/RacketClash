package com.olt.racketclash.state.team

import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamDatabase
import com.olt.racketclash.state.detail.DetailModel

class TeamModel(
    private val teamDatabase: TeamDatabase,
    private val playerDatabase: PlayerDatabase,
    private val teamId: Long
) : DetailModel<Team, TeamData>(
    initialItem = Team(),
    initialData = TeamData()
) {

    override suspend fun databaseUpdate(item: Team) =
        teamDatabase.update(team = item)

    override suspend fun databaseSelectItem(): Team =
        teamDatabase.selectSingle(id = teamId)

    override suspend fun databaseSelectData(item: Team): TeamData =
        TeamData(
            players = playerDatabase.selectLast(n = 5)
        )
}