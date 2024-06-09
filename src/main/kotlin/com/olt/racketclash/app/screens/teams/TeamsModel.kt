package com.olt.racketclash.app.screens.teams

import com.olt.racketclash.data.Project
import com.olt.racketclash.data.database.Database
import com.olt.racketclash.data.Team
import com.olt.racketclash.data.sort
import com.olt.racketclash.state.ViewModelState

class TeamsModel(
    private val database: Database,
    private val project: Project
) : ViewModelState<TeamsModel.State>(initialState = State()) {

    private var completeTeams: List<Team> = emptyList()

    init {
        onIO {
            database.teams().collect { teamList ->
                val teams = teamList.toMutableList()

                teams.replaceAll {
                    it.copy(
                        wonGames = it.wonGames + (project.gamePointsForBye * it.bye),
                        wonSets = it.wonSets + (project.setPointsForBye * it.bye),
                        wonPoints = it.wonPoints + (project.pointsForBye * it.bye)
                    )
                }

                completeTeams = teams.toList()

                updateState {
                    copy(
                        isLoading = false,
                        teams = teams.toList().filter { it.name.contains(filter) }.sort(sortedBy = sortedBy)
                    )
                }
            }
        }
    }

    data class State(
        val isLoading: Boolean = true,
        val teams: List<Team> = emptyList(),
        val filter: String = "",
        val sortedBy: Team.Sorting = Team.Sorting.NameAscending
    )

    fun deleteTeam(id: Long) =
        onIO {
            if (state.value.teams.find { it.id == id }?.size == 0) {
                database.deleteTeam(id = id)
            }
        }

    fun changeFilter(newFilter: String) =
        onDefault {
            updateState {
                copy(
                    filter = newFilter,
                    teams = completeTeams.filter { it.name.contains(newFilter) }.sort(sortedBy = sortedBy)
                )
            }
        }

    fun changeSorting(newSorting: Team.Sorting) =
        onDefault {
            updateState {
                copy(
                    sortedBy = newSorting,
                    teams = completeTeams.filter { it.name.contains(filter) }.sort(sortedBy = newSorting)
                )
            }
        }
}