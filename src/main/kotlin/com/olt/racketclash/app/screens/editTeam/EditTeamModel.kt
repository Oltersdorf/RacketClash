package com.olt.racketclash.app.screens.editTeam

import com.olt.racketclash.data.database.Database
import com.olt.racketclash.state.ViewModelState

class EditTeamModel(
    private val database: Database,
    private val teamId: Long?,
    private val projectId: Long
) : ViewModelState<EditTeamModel.State>(initialState = State(projectId = projectId)) {

    init {
        onIO {
            if (teamId != null) {
                database.team(id = teamId).collect {
                    if (it != null)
                        updateState { copy(name = it.name, strength = it.strength) }
                }
            }
        }
    }

    data class State(
        val projectId: Long,
        val name: String = "",
        val strength: Int = 1
    )

    fun changeName(newName: String) =
        onDefault {
            updateState { copy(name = newName) }
        }

    fun changeStrength(newStrength: Int) =
        onDefault {
            updateState { copy(strength = newStrength) }
        }

    fun updateTeam() =
        onIO {
            if (teamId == null)
                database.addTeam(name = state.value.name, strength = state.value.strength, projectId = projectId)
            else
                database.updateTeam(id = teamId, name = state.value.name, strength = state.value.strength, projectId = projectId)
        }
}