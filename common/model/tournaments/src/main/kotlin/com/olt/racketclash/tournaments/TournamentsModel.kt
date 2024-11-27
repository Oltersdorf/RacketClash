package com.olt.racketclash.tournaments

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.tournament.Sorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class TournamentsModel(
    private val database: Database
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

    fun onSort(sorting: Sorting) =
        updateTournamentsState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateTournamentsState(currentPage = pageNumber)

    fun deleteTournament(id: Long) {
        onIO {
            updateState { copy(tournaments = tournaments.toMutableList().apply { removeIf { it.id == id } }) }
            database.tournaments.delete(id = id)
        }
    }

    private fun updateTournamentsState(
        sorting: Sorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val (totalSize, sortedTournaments) =
                database.tournaments.selectFilteredAndOrdered(
                    nameFilter = filters.name ?: "",
                    locationFilter = filters.location ?: "",
                    yearFilter = filters.year,
                    sorting = sorting,
                    fromIndex = (currentPage - 1) * pageSize,
                    toIndex = currentPage * pageSize
                )

            updateState {
                copy(
                    isLoading = false,
                    tournaments = sortedTournaments,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}