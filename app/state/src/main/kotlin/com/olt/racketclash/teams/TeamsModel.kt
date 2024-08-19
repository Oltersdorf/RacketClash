package com.olt.racketclash.teams

import com.olt.racketclash.data.ProjectSettings
import com.olt.racketclash.data.Team
import com.olt.racketclash.data.database.IProjectDatabase
import com.olt.racketclash.data.database.ITeamDatabase
import com.olt.racketclash.data.sort
import com.olt.racketclash.state.ViewModelState

class TeamsModel(
    private val projectDatabase: IProjectDatabase,
    private val teamDatabase: ITeamDatabase,
    private val projectId: Long
) : ViewModelState<TeamsModel.State>(initialState = State(projectId = projectId)) {

    private var completeTeams: List<Team> = emptyList()
    private var projectSettings: ProjectSettings? = null

    init {
        onIO {
            projectDatabase.projectSettings(id = projectId).collect { settings ->
                projectSettings = settings

                if (settings != null) {
                    val teams = completeTeams.toMutableList()
                    teams.replaceAll {
                        it.copy(
                            wonGames = it.wonGames + (settings.gamePointsForBye * it.bye),
                            wonSets = it.wonSets + (settings.setPointsForBye * it.bye),
                            wonPoints = it.wonPoints + (settings.pointsForBye * it.bye)
                        )
                    }
                    completeTeams = teams.toList()
                }
            }
        }

        onIO {
            teamDatabase.teams().collect { teamList ->
                val teams = teamList.toMutableList()

                val settings = projectSettings
                if (settings != null) {
                    teams.replaceAll {
                        it.copy(
                            wonGames = it.wonGames + (settings.gamePointsForBye * it.bye),
                            wonSets = it.wonSets + (settings.setPointsForBye * it.bye),
                            wonPoints = it.wonPoints + (settings.pointsForBye * it.bye)
                        )
                    }
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
        val projectId: Long,
        val isLoading: Boolean = true,
        val teams: List<Team> = emptyList(),
        val filter: String = "",
        val sortedBy: Team.Sorting = Team.Sorting.NameAscending
    )

    fun deleteTeam(id: Long) =
        onIO {
            if (state.value.teams.find { it.id == id }?.size == 0) {
                teamDatabase.delete(id = id, projectId = projectId)
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