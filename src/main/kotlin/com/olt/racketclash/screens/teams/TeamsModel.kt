package com.olt.racketclash.screens.teams

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Team
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
                        teams = teamList.sortAndFilter(filter = filter, sortedBy = sortedBy)
                    )
                }
            }
        }
    }

    sealed class Sorting {
        data object NameAscending : Sorting()
        data object NameDescending : Sorting()
        data object StrengthAscending : Sorting()
        data object StrengthDescending : Sorting()
        data object PointsAscending : Sorting()
        data object PointsDescending : Sorting()
    }

    data class Modal(
        val isLoading: Boolean = true,
        val teams: List<Team> = emptyList(),
        val filter: String = "",
        val availableSorting: List<Sorting> =
            listOf(
                Sorting.NameAscending, Sorting.NameDescending,
                Sorting.StrengthAscending, Sorting.StrengthDescending,
                Sorting.PointsAscending, Sorting.PointsDescending
            ),
        val sortedBy: Sorting = Sorting.NameAscending
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
                    teams = completeTeams.sortAndFilter(filter = newFilter, sortedBy = sortedBy)
                )
            }
        }
    }

    fun changeSorting(newSorting: Sorting) {
        screenModelScope.launch(context = Dispatchers.Default) {
            updateState {
                copy(
                    sortedBy = newSorting,
                    teams = completeTeams.sortAndFilter(filter = filter, sortedBy = newSorting)
                )
            }
        }
    }

    private fun List<Team>.sortAndFilter(filter: String, sortedBy: Sorting): List<Team> {
        val teams = filter { it.name.contains(filter) }

        return when (sortedBy) {
            Sorting.NameAscending -> teams.sortedBy { it.name }
            Sorting.NameDescending -> teams.sortedByDescending { it.name }
            Sorting.PointsAscending -> teams.sortedWith(compareBy(Team::wonGames, Team::lostGames, Team::wonSets, Team::lostSets, Team::wonPoints, Team::lostPoints))
            Sorting.PointsDescending -> teams.sortedWith(compareBy(Team::wonGames, Team::lostGames, Team::wonSets, Team::lostSets, Team::wonPoints, Team::lostPoints).reversed())
            Sorting.StrengthAscending -> teams.sortedBy { it.strength }
            Sorting.StrengthDescending -> teams.sortedByDescending { it.strength }
        }
    }

}