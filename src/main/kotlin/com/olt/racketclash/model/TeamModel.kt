package com.olt.racketclash.model

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamModel(
    private val database: Database,
    private val updateProjectTeams: (teamNumber: Int) -> Unit
) : StateScreenModel<TeamModel.Modal>(Modal()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.teams().collect { teamList ->
                mutableState.value = Modal(
                    isLoading = false,
                    teams = teamList
                )
                updateProjectTeams(teamList.size)
            }
        }
    }

    data class Modal(
        val isLoading: Boolean = true,
        val teams: List<Team> = emptyList()
    )

    fun updateTeam(id: Long?, name: String, strength: Int) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (id == null)
                database.addTeam(name = name, strength = strength)
            else
                database.updateTeam(id = id, name = name, strength = strength)
        }
    }

    fun deleteTeam(id: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (mutableState.value.teams.find { it.id == id }?.size == 0) {
                database.deleteTeam(id = id)
            }
        }
    }
}