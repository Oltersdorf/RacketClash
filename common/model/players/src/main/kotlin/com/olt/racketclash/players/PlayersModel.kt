package com.olt.racketclash.players

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.player.Sorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class PlayersModel(
    private val database: Database
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        updatePlayersState()
    }

    fun updateSearchBar(text: String) {
        updateState { copy(searchBarText = text) }

        val textAsNumber = text.toIntOrNull()

        updateState {
            copy(availableTags = Tags(
                name = if (tags.name == null) text else null,
                birthYear = if (tags.birthYear == null && textAsNumber != null) textAsNumber else null,
                club = if (tags.name == null) text else null
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

        updatePlayersState()
    }

    fun removeNameTag() {
        updateState {
            copy(
                tags = tags.copy(name = null),
                availableTags = availableTags.copy(name = searchBarText)
            )
        }

        updatePlayersState()
    }

    fun addBirthYearTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(birthYear = null),
                tags = tags.copy(birthYear = availableTags.birthYear)
            )
        }

        updatePlayersState()
    }

    fun removeBirthYearTag() {
        updateState {
            copy(
                tags = tags.copy(birthYear = null),
                availableTags = availableTags.copy(birthYear = searchBarText.toIntOrNull())
            )
        }

        updatePlayersState()
    }

    fun addClubTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(club = null),
                tags = tags.copy(club = availableTags.club)
            )
        }

        updatePlayersState()
    }

    fun removeClubTag() {
        updateState {
            copy(
                tags = tags.copy(club = null),
                availableTags = availableTags.copy(club = searchBarText)
            )
        }

        updatePlayersState()
    }

    fun addHasMedalsTag(value: Boolean) {
        updateState {
            copy(
                availableTags = availableTags.copy(hasMedals = null),
                tags = tags.copy(hasMedals = value)
            )
        }

        updatePlayersState()
    }

    fun removeHasMedalsTag() {
        updateState {
            copy(
                tags = tags.copy(hasMedals = null),
                availableTags = availableTags.copy(hasMedals = true)
            )
        }

        updatePlayersState()
    }

    fun onSort(sorting: Sorting) =
        updatePlayersState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updatePlayersState(currentPage = pageNumber)

    fun deletePlayer(id: Long) {
        onIO {
            updateState { copy(players = players.toMutableList().apply { removeIf { it.id == id } }) }
            database.players.delete(id = id)
        }
    }

    private fun updatePlayersState(
        sorting: Sorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val (totalSize, sortedPlayers) =
                database.players.selectFilteredAndOrdered(
                    nameFilter = filters.name ?: "",
                    birthYearFilter = filters.birthYear,
                    clubFilter = filters.club ?: "",
                    hasMedalsFilter = filters.hasMedals,
                    sorting = sorting,
                    fromIndex = (currentPage - 1) * pageSize,
                    toIndex = currentPage * pageSize
                )

            updateState {
                copy(
                    isLoading = false,
                    players = sortedPlayers,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}