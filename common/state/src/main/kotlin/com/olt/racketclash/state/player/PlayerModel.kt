package com.olt.racketclash.state.player

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.ViewModelState

class PlayerModel(
    private val playerDatabase: PlayerDatabase,
    tournamentDatabase: TournamentDatabase,
    categoryDatabase: CategoryDatabase,
    gameDatabase: GameDatabase,
    id: Long
) : ViewModelState<PlayerState>(initialState = PlayerState()) {

    init {
        onIO {
            val player = playerDatabase.selectSingle(id = id)
            val tournaments = tournamentDatabase.selectLast(n = 5)
            val categories = categoryDatabase.selectLast(n = 5)
            val games = gameDatabase.selectLast(n = 5)
            val clubSuggestions = playerDatabase.clubs(filter = player.club)

            updateState {
                copy(
                    isLoading = false,
                    player = player,
                    tournaments = tournaments,
                    categories = categories,
                    games = games,
                    clubSuggestions = clubSuggestions
                )
            }
        }
    }

    fun updatePlayer(player: Player) {
        onIO {
            updateState { copy(player = player) }
            playerDatabase.update(player = player)
        }
    }

    fun clubSuggestions(filter: String) {
        onIO {
            val clubSuggestions = playerDatabase.clubs(filter = filter)
            updateState { copy(clubSuggestions = clubSuggestions) }
        }
    }
}