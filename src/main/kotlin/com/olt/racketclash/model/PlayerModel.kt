package com.olt.racketclash.model

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerModel(
    private val database: Database
) : StateScreenModel<PlayerModel.Modal>(Modal()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.player().collect { playerList ->
                mutableState.value = mutableState.value.copy(
                    isLoading = false,
                    player = playerList
                )
            }
        }
        screenModelScope.launch(context = Dispatchers.IO) {
            database.teams().collect { teamsList ->
                mutableState.value = mutableState.value.copy(
                    teams = teamsList
                )
            }
        }
    }

    data class Modal(
        val isLoading: Boolean = true,
        val player: List<Player> = emptyList(),
        val teams: List<Team> = emptyList()
    )

    fun updatePlayer(id: Long?, name: String, teamId: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (id == null)
                database.addPlayer(name = name, teamId = teamId)
            else
                database.updatePlayer(id = id, name = name, teamId = teamId)
        }
    }

    fun updateActive(id: Long, active: Boolean) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.playerSetActive(id = id, active = active)
        }
    }

    fun deletePlayer(id: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {

        }
    }
}