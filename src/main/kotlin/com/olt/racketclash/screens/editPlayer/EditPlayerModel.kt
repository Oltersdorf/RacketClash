package com.olt.racketclash.screens.editPlayer

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlayerModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database,
    player: Player?
) : NavigableStateScreenModel<EditPlayerModel.Model>(navigateToScreen, Model(player = player)) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.teams().collect {
                updateState { copy(teams = it, selectedTeam = it.find { it.id == player?.teamId } ?: noTeamSelected) }
            }
        }
    }

    companion object {
        val noTeamSelected: Team
            get() = Team(name = "<No Team Selected>", id = -1L, strength = 0, size = 0)
    }

    data class Model(
        val player: Player?,
        val teams: List<Team> = emptyList(),
        val selectedTeam: Team = noTeamSelected
    )

    fun updatePlayer(id: Long?, name: String, teamId: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (id == null)
                database.addPlayer(name = name, teamId = teamId)
            else
                database.updatePlayer(id = id, name = name, teamId = teamId)
        }
    }

    fun selectTeam(id: Long) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState { copy(selectedTeam = teams.find { it.id == id } ?: noTeamSelected) }
        }
    }
}