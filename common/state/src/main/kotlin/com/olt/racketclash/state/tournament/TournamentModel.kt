package com.olt.racketclash.state.tournament

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.detail.DetailModel

class TournamentModel(
    private val tournamentDatabase: TournamentDatabase,
    private val playerDatabase: PlayerDatabase,
    private val teamDatabase: TeamDatabase,
    private val categoryDatabase: CategoryDatabase,
    private val gameDatabase: GameDatabase,
    private val scheduleDatabase: ScheduleDatabase,
    private val tournamentId: Long
) : DetailModel<Tournament, TournamentData>(
    initialItem = Tournament(),
    initialData = TournamentData()
) {

    override suspend fun databaseUpdate(item: Tournament) =
        tournamentDatabase.update(tournament = item)

    override suspend fun databaseSelectItem(): Tournament =
        tournamentDatabase.selectSingle(id = tournamentId)

    override suspend fun databaseSelectData(item: Tournament): TournamentData =
        TournamentData(
            players = playerDatabase.selectLast(n = 5),
            teams = teamDatabase.selectLast(n = 5),
            categories = categoryDatabase.selectLast(n = 5),
            games = gameDatabase.selectLast(n = 5),
            scheduledGames = scheduleDatabase.selectFirst(n = 5),
            locationSuggestions = tournamentDatabase.locations(filter = item.location)
        )

    fun locationSuggestions(filter: String) {
        onIO {
            val locationSuggestions = tournamentDatabase.locations(filter = filter)
            updateData { copy(locationSuggestions = locationSuggestions) }
        }
    }
}