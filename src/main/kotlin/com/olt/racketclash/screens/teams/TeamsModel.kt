package com.olt.racketclash.screens.teams

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamsModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<TeamsModel.Modal>(navigateToScreen, Modal()) {

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.teams().collect { teamList ->
                mutableState.value = Modal(
                    isLoading = false,
                    teams = teamList
                )
            }
        }
    }

    data class Modal(
        val isLoading: Boolean = true,
        val teams: List<Team> = emptyList()
    )

    fun deleteTeam(id: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (mutableState.value.teams.find { it.id == id }?.size == 0) {
                database.deleteTeam(id = id)
            }
        }
    }
}