package com.olt.racketclash.addorupdateplayer

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState

class AddOrUpdatePlayerModel(
    private val database: Database,
    private val playerId: Long?
) : ViewModelState<State>(initialState = State()) {

    fun updateName(newName: String) =
        updateState { copy(name = newName, isSavable = newName.isNotBlank()) }

    fun updateBirthYear(newBirthYear: Int) =
        updateState { copy(birthYear = newBirthYear) }

    fun updateClub(newClub: String) {
        updateState { copy(club = newClub) }

        onIO {
            //update suggestedClubs
        }
    }

    fun save() =
        onIO {
            //write to database
        }
}