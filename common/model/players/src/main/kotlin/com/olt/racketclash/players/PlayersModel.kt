package com.olt.racketclash.players

import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.database.api.PlayerFilter
import com.olt.racketclash.database.api.PlayerSorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class PlayersModel(
    private val database: PlayerDatabase
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

    fun onSort(sorting: PlayerSorting) =
        updatePlayersState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updatePlayersState(currentPage = pageNumber)

    fun deletePlayer(player: Player) {
        onIO {
            database.delete(id = player.id)
            updatePlayersState()
        }
    }

    private fun updatePlayersState(
        sorting: PlayerSorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val players =
                database.selectList(
                    filter = PlayerFilter(
                        name = filters.name ?: "",
                        club = filters.club ?: ""
                    ),
                    sorting = sorting,
                    fromIndex = (currentPage - 1) * pageSize.toLong(),
                    toIndex = currentPage * pageSize.toLong()
                )

            updateState {
                copy(
                    isLoading = false,
                    players = players.items,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((players.totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}