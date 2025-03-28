package com.olt.racketclash.tournaments

import com.olt.racketclash.database.api.TournamentDatabase
import com.olt.racketclash.database.api.TournamentFilter
import com.olt.racketclash.database.api.TournamentSorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class TournamentsModel(
    private val database: TournamentDatabase
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        updateTournamentsState()
    }

    fun updateSearchBar(text: String) {
        updateState { copy(searchBarText = text) }

        val textAsNumber = text.toIntOrNull()

        updateState {
            copy(availableTags = Tags(
                name = if (tags.name == null) text else null,
                location = if (tags.location == null) text else null,
                year = if (tags.year == null && textAsNumber != null) textAsNumber else null
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

        updateTournamentsState()
    }

    fun removeNameTag() {
        updateState {
            copy(
                tags = tags.copy(name = null),
                availableTags = availableTags.copy(name = searchBarText)
            )
        }

        updateTournamentsState()
    }

    fun addLocationTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(location = null),
                tags = tags.copy(location = availableTags.location)
            )
        }

        updateTournamentsState()
    }

    fun removeLocationTag() {
        updateState {
            copy(
                tags = tags.copy(location = null),
                availableTags = availableTags.copy(location = searchBarText)
            )
        }

        updateTournamentsState()
    }

    fun addYearTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(year = null),
                tags = tags.copy(year = availableTags.year)
            )
        }

        updateTournamentsState()
    }

    fun removeYearTag() {
        updateState {
            copy(
                tags = tags.copy(year = null),
                availableTags = availableTags.copy(year = searchBarText.toIntOrNull())
            )
        }

        updateTournamentsState()
    }

    fun onSort(sorting: TournamentSorting) =
        updateTournamentsState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateTournamentsState(currentPage = pageNumber)

    fun deleteTournament(id: Long) {
        onIO {
            updateState { copy(tournaments = tournaments.toMutableList().apply { removeIf { it.id == id } }) }
            database.delete(id = id)
        }
    }

    private fun updateTournamentsState(
        sorting: TournamentSorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val tournaments =
                database.selectList(
                    filter = TournamentFilter(
                        name = filters.name ?: "",
                        location = filters.location ?: ""
                    ),
                    sorting = sorting,
                    fromIndex = (currentPage - 1) * pageSize.toLong(),
                    toIndex = currentPage * pageSize.toLong()
                )

            updateState {
                copy(
                    isLoading = false,
                    tournaments = tournaments.items,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((tournaments.totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}