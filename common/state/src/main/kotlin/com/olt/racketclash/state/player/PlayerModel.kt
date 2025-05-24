package com.olt.racketclash.state.player

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.detail.DetailModel

class PlayerModel(
    private val playerDatabase: PlayerDatabase,
    private val tournamentDatabase: TournamentDatabase,
    private val categoryDatabase: CategoryDatabase,
    private val gameDatabase: GameDatabase,
    private val playerId: Long
) : DetailModel<Player, PlayerData>(
    initialItem = Player(),
    initialData = PlayerData()
) {

    override suspend fun databaseUpdate(item: Player) =
        playerDatabase.update(player = item)

    override suspend fun databaseSelectItem(): Player =
        playerDatabase.selectSingle(id = playerId)

    override suspend fun databaseSelectData(item: Player): PlayerData =
        PlayerData(
            tournaments = tournamentDatabase.selectLast(n = 5),
            categories = categoryDatabase.selectLast(n = 5),
            games = gameDatabase.selectLast(n = 5),
            clubSuggestions = playerDatabase.clubs(filter = item.club)
        )

    fun clubSuggestions(filter: String) {
        onIO {
            val clubSuggestions = playerDatabase.clubs(filter = filter)
            updateData { copy(clubSuggestions = clubSuggestions) }
        }
    }
}