package com.olt.racketclash.screens.teams

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Team
import com.olt.racketclash.data.sort
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamsModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<TeamsModel.Modal>(navigateToScreen, Modal()) {

    private var completeTeams: List<Team> = emptyList()

    init {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.teams().collect { teamList ->
                updateState {
                    completeTeams = teamList
                    copy(
                        isLoading = false,
                        teams = teamList.filter { it.name.contains(filter) }.sort(sortedBy = sortedBy)
                    )
                }
            }
        }
    }

    data class Modal(
        val isLoading: Boolean = true,
        val teams: List<Team> = emptyList(),
        val filter: String = "",
        val sortedBy: Team.Sorting = Team.Sorting.NameAscending
    )

    fun deleteTeam(id: Long) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (mutableState.value.teams.find { it.id == id }?.size == 0) {
                database.deleteTeam(id = id)
            }
        }
    }

    fun changeFilter(newFilter: String) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    filter = newFilter,
                    teams = completeTeams.filter { it.name.contains(newFilter) }.sort(sortedBy = sortedBy)
                )
            }
        }
    }

    fun changeSorting(newSorting: Team.Sorting) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    sortedBy = newSorting,
                    teams = completeTeams.filter { it.name.contains(filter) }.sort(sortedBy = newSorting)
                )
            }
        }
    }
}