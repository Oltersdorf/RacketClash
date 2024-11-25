package com.olt.racketclash.addorupdateplayer

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState

class AddOrUpdatePlayerModel(
    private val database: Database,
    private val playerId: Long?
) : ViewModelState<State>(initialState = State()) {

    init {
        onIO {
            if (playerId == null)
                updateState { copy(isLoading = false) }
            else {
                updateState {
                    copy(
                        isSavable = true,
                        isLoading = false,
                        player = database.players.selectSingle(id = playerId)
                    )
                }
            }

            updateState {
                copy(clubSuggestions = database.players.clubs(clubFilter = player.club))
            }
        }
    }

    fun updateName(newName: String) =
        updateState { copy(isSavable = newName.isNotBlank(), player = player.copy(name = newName)) }

    fun updateBirthYear(newBirthYear: Int) =
        updateState { copy(player = player.copy(birthYear = newBirthYear)) }

    fun updateClub(newClub: String) {
        updateState { copy(player = player.copy(club = newClub)) }

        onIO {
            updateState {
                copy(clubSuggestions = database.players.clubs(clubFilter = newClub))
            }
        }
    }

    fun save() =
        onIO {
            if (playerId == null)
                database.players.add(player = state.value.player)
            else
                database.players.update(player = state.value.player)
        }
}