package com.olt.racketclash.teams

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.table.FilteredAndOrderedTeam
import com.olt.racketclash.database.team.Sorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class TeamsModel(
    private val database: Database,
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

    fun onSort(sorting: Sorting) =
        updateTeamState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateTeamState(currentPage = pageNumber)

    fun onDelete(team: FilteredAndOrderedTeam) {
        updateState { copy(teams = teams - team) }
        onIO {
            database.teams.delete(id = team.id)
            updateTeamState()
        }
    }

    private fun updateTeamState(
        sorting: Sorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val (totalSize, sortedTeams) = database.teams.selectFilteredAndOrdered(
                tournamentId = tournamentId,
                nameFilter = filters.name ?: "",
                rankFilter = filters.rank,
                sorting = sorting,
                fromIndex = (currentPage - 1) * pageSize,
                toIndex = currentPage * pageSize
            )

            updateState {
                copy(
                    isLoading = false,
                    teams = sortedTeams,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}