package com.olt.racketclash.editPlayer

import com.olt.racketclash.data.Team
import com.olt.racketclash.data.database.IPlayerDatabase
import com.olt.racketclash.data.database.ITeamDatabase
import com.olt.racketclash.state.ViewModelState

class EditPlayerModel(
    private val teamDatabase: ITeamDatabase,
    private val playerDatabase: IPlayerDatabase,
    private val playerId: Long?,
    private val projectId: Long
) : ViewModelState<EditPlayerModel.State>(initialState = State(projectId = projectId)) {

    init {
        onIO {
            teamDatabase.teams().collect {
                updateState { copy(teams = it, selectedTeam = it.find { it.id == teamId } ?: noTeamSelected) }
            }
        }
        onIO {
            if (playerId != null) {
                playerDatabase.player(id = playerId).collect { player ->
                    if (player != null)
                        updateState {
                            copy(
                                name = player.name,
                                selectedTeam = teams.find { it.id == player.teamId } ?: noTeamSelected,
                                teamId = player.teamId
                            )
                        }
                }
            }
        }
    }

    companion object {
        val noTeamSelected: Team
            get() = Team(name = "<No Team Selected>", id = -1L, strength = 0, size = 0)
    }

    data class State(
        val projectId: Long,
        val name: String = "",
        val teamId: Long? = null,
        val teams: List<Team> = emptyList(),
        val selectedTeam: Team = noTeamSelected
    )

    fun updatePlayer() =
        onIO {
            if (playerId == null)
                playerDatabase.add(name = state.value.name, teamId = state.value.selectedTeam.id, projectId = projectId)
            else
                playerDatabase.update(id = playerId, name = state.value.name, teamId = state.value.selectedTeam.id, projectId = projectId)
        }

    fun selectTeam(id: Long) =
        onDefault {
            updateState { copy(selectedTeam = teams.find { it.id == id } ?: noTeamSelected) }
        }

    fun changeName(newName: String) =
        onDefault {
            updateState { copy(name = newName) }
        }
}