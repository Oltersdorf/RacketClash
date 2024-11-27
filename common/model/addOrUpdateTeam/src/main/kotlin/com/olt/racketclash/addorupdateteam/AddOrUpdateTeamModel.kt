package com.olt.racketclash.addorupdateteam

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState

class AddOrUpdateTeamModel(
    private val database: Database,
    private val teamId: Long?,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    init {
        onIO {
            if (teamId == null)
                updateState { copy(isLoading = false) }
            else {
                updateState {
                    copy(
                        isSavable = true,
                        isLoading = false,
                        team = database.teams.selectSingle(id = teamId)
                    )
                }
            }
        }
    }

    fun updateName(newName: String) =
        updateState { copy(team = team.copy(name = newName), isSavable = newName.isNotBlank()) }

    fun save(onComplete: () -> Unit = {}) =
        onIO {
            updateState { copy(isLoading = true) }

            if (teamId == null)
                database.teams.add(team = state.value.team, tournamentId = tournamentId)
            else
                database.teams.update(team = state.value.team)

            updateState { copy(isLoading = false) }

            onMain { onComplete() }
        }
}