package com.olt.racketclash.teams

import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamDatabase
import com.olt.racketclash.database.api.TeamFilter
import com.olt.racketclash.database.api.TeamSorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class TeamsModel(
    private val database: TeamDatabase,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        updateTeamState()
    }

    fun updateSearchBar(text: String) {
        updateState { copy(searchBarText = text) }

        val number = text.toIntOrNull()

        updateState {
            copy(availableTags = Tags(
                name = if (tags.name == null) text else null,
                rank = if (tags.rank == null && number != null) number else null
            ))
        }
    }

    fun addNameTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(name = null),
                tags = tags.copy(name = availableTags.name)
            )
        }

        updateTeamState()
    }

    fun removeNameTag() {
        updateState {
            copy(
                tags = tags.copy(name = null),
                availableTags = availableTags.copy(name = searchBarText)
            )
        }

        updateTeamState()
    }

    fun addRankTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(rank = null),
                tags = tags.copy(rank = availableTags.rank)
            )
        }

        updateTeamState()
    }

    fun removeRankTag() {
        updateState {
            copy(
                tags = tags.copy(rank = null),
                availableTags = availableTags.copy(rank = searchBarText.toIntOrNull())
            )
        }

        updateTeamState()
    }

    fun onSort(sorting: TeamSorting) =
        updateTeamState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateTeamState(currentPage = pageNumber)

    fun onDelete(team: Team) {
        updateState { copy(teams = teams - team) }
        onIO {
            database.delete(id = team.id)
            updateTeamState()
        }
    }

    private fun updateTeamState(
        sorting: TeamSorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val teams = database.selectList(
                filter = TeamFilter(
                    tournamentId = tournamentId,
                    name = filters.name ?: ""
                ),
                sorting = sorting,
                fromIndex = (currentPage - 1) * pageSize.toLong(),
                toIndex = currentPage * pageSize.toLong()
            )

            updateState {
                copy(
                    isLoading = false,
                    teams = teams.items,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((teams.totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}