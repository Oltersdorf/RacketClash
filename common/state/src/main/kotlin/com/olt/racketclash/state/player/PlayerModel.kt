package com.olt.racketclash.state.player

import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.state.ViewModelState

class PlayerModel(
    private val playerDatabase: PlayerDatabase,
    id: Long
) : ViewModelState<PlayerState>(initialState = PlayerState()) {

    init {
        onIO {
            val player = playerDatabase.selectSingle(id = id)
            val clubSuggestions = playerDatabase.clubs(filter = player.club)

            updateState {
                copy(
                    isLoading = false,
                    clubSuggestions = clubSuggestions,
                    player = player
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