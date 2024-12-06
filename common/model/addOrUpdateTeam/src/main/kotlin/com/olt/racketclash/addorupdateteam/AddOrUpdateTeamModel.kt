package com.olt.racketclash.addorupdateteam

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.player.Sorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class AddOrUpdateTeamModel(
    private val database: Database,
    private val teamId: Long?,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        onIO {
            if (teamId == null)
                updateState { copy(isLoading = false) }
            else {
                updateState {
                    copy(
                        isSavable = true,
                        isLoading = false,
                        team = database.teams.selectSingle(id = teamId),
                        selectedPlayers = database.playerToTeam.playerIn(teamId = teamId)
                    )
                }
            }

            updatePlayersState()
        }
    }

    fun updateName(newName: String) =
        updateState { copy(team = team.copy(name = newName), isSavable = newName.isNotBlank()) }

    fun updateRank(number: Int) =
        updateState { copy(team = team.copy(rank = number)) }

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

    fun onAddSelect(playerId: Long) =
        updateState { copy(selectedPlayers = selectedPlayers + playerId) }

    fun onRemoveSelect(playerId: Long) =
        updateState { copy(selectedPlayers = selectedPlayers - playerId) }

    fun onSort(sorting: Sorting) =
        updatePlayersState(sorting = sorting)

    fun changePage(number: Int) =
        updatePlayersState(currentPage = number)

    fun save(onComplete: () -> Unit = {}) =
        onIO {
            updateState { copy(isLoading = true) }

            if (teamId == null)
                database.teams.add(team = state.value.team, tournamentId = tournamentId, players = state.value.selectedPlayers)
            else
                database.teams.update(team = state.value.team, players = state.value.selectedPlayers)

            updateState { copy(isLoading = false) }

            onMain { onComplete() }
        }

    private fun updatePlayersState(
        sorting: Sorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(playersLoading = true) }

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
                    playersLoading = false,
                    players = sortedPlayers,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}