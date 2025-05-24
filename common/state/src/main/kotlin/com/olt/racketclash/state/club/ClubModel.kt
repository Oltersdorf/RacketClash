package com.olt.racketclash.state.club

import com.olt.racketclash.database.api.Club
import com.olt.racketclash.database.api.ClubDatabase
import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.database.api.TournamentDatabase
import com.olt.racketclash.state.detail.DetailModel

class ClubModel(
    private val clubDatabase: ClubDatabase,
    private val playerDatabase: PlayerDatabase,
    private val tournamentDatabase: TournamentDatabase,
    private val clubId: Long
) : DetailModel<Club, ClubData>(
    initialItem = Club(),
    initialData = ClubData()
) {

    override suspend fun databaseSelectItem(): Club =
        clubDatabase.selectSingle(id = clubId)

    override suspend fun databaseUpdate(item: Club) =
        clubDatabase.update(club = item)

    override suspend fun databaseSelectData(item: Club): ClubData =
        ClubData(
            players = playerDatabase.selectLast(n = 5),
            tournaments = tournamentDatabase.selectLast(n = 5)
        )
}