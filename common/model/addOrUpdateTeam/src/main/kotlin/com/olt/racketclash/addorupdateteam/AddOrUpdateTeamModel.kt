package com.olt.racketclash.addorupdateteam

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState

class AddOrUpdateTeamModel(
    private val database: Database,
    private val teamId: Long?,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    fun updateName(newName: String) =
        updateState { copy(name = newName, isSavable = newName.isNotBlank()) }

    fun save() =
        onIO {
            //write to database
        }
}