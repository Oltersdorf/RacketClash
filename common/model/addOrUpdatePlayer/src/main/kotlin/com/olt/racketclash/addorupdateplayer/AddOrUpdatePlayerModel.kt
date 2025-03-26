package com.olt.racketclash.addorupdateplayer

import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.state.ViewModelState

class AddOrUpdatePlayerModel(
    private val database: PlayerDatabase,
    private val playerId: Long?
) : ViewModelState<State>(initialState = State()) {

    init {
        onIO {
            if (playerId == null)
                updateState { copy(isLoading = false) }
            else {
                val player = database.selectSingle(id = playerId)
                updateState {
                    copy(
                        isSavable = true,
                        isLoading = false,
                        player = player
                    )
                }
            }

            val clubSuggestions = database.clubs(filter = state.value.player.club)
            updateState { copy(clubSuggestions = clubSuggestions) }
        }
    }

    fun updateName(newName: String) =
        updateState { copy(isSavable = newName.isNotBlank(), player = player.copy(name = newName)) }

    fun updateBirthYear(newBirthYear: Int) =
        updateState { copy(player = player.copy(birthYear = newBirthYear)) }

    fun updateClub(newClub: String) {
        updateState { copy(player = player.copy(club = newClub)) }

        onIO {
            val clubSuggestions = database.clubs(filter = newClub)
            updateState { copy(clubSuggestions = clubSuggestions) }
        }
    }

    fun save(onComplete: () -> Unit = {}) =
        onIO {
            updateState { copy(isLoading = true) }

            if (playerId == null)
                database.add(player = state.value.player)
            else
                database.update(player = state.value.player)

            updateState { copy(isLoading = true) }

            onMain { onComplete() }
        }
}