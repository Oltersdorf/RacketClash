package com.olt.racketclash.schedule

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.schedule.Sorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class ScheduleModel(
    private val database: Database,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        updateScheduleState()
    }

    fun updateSearchBar(text: String) {
        updateState { copy(searchBarText = text) }

        updateState {
            copy(availableTags = availableTags.copy(
                category = if (tags.category == null) text else null,
                player = if (tags.player == null) text else null
            ))
        }
    }

    fun addActiveTag(value: Boolean) {
        updateState {
            copy(
                availableTags = availableTags.copy(active = null),
                tags = tags.copy(active = value)
            )
        }

        updateScheduleState()
    }

    fun removeActiveTag() {
        updateState {
            copy(
                tags = tags.copy(active = null),
                availableTags = availableTags.copy(active = true)
            )
        }

        updateScheduleState()
    }

    fun addSinglesTag(value: Boolean) {
        updateState {
            copy(
                availableTags = availableTags.copy(singles = null),
                tags = tags.copy(singles = value)
            )
        }

        updateScheduleState()
    }

    fun removeSinglesTag() {
        updateState {
            copy(
                tags = tags.copy(singles = null),
                availableTags = availableTags.copy(singles = true)
            )
        }

        updateScheduleState()
    }

    fun addCategoryTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(category = null),
                tags = tags.copy(category = availableTags.category)
            )
        }

        updateScheduleState()
    }

    fun removeCategoryTag() {
        updateState {
            copy(
                tags = tags.copy(category = null),
                availableTags = availableTags.copy(category = searchBarText)
            )
        }

        updateScheduleState()
    }

    fun addPlayerTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(player = null),
                tags = tags.copy(player = availableTags.player)
            )
        }

        updateScheduleState()
    }

    fun removePlayerTag() {
        updateState {
            copy(
                tags = tags.copy(player = null),
                availableTags = availableTags.copy(player = searchBarText)
            )
        }

        updateScheduleState()
    }

    fun onSort(sorting: Sorting) =
        updateScheduleState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateScheduleState(currentPage = pageNumber)

    fun onSaveResult(
        scheduledGameId: Long,
        sets: List<Pair<Int, Int>>
    ) =
        onIO {
            val scheduledGame = state.value.scheduledGames.find { it.id == scheduledGameId }
            updateState { copy(scheduledGames = scheduledGames.toMutableList().apply { remove(scheduledGame) }) }
            scheduledGame?.let { database.schedule.setComplete(scheduledGame = it, sets = sets) }
        }

    private fun updateScheduleState(
        sorting: Sorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val (totalSize, sortedSchedules) =
                database.schedule.selectFilteredAndOrdered(
                    tournamentId = tournamentId,
                    categoryNameFilter = filters.category ?: "",
                    isSingleFilter = filters.singles,
                    isActiveFilter = filters.active,
                    playerNameFilter = filters.player ?: "",
                    sorting = sorting,
                    fromIndex = (currentPage - 1) * pageSize,
                    toIndex = currentPage * pageSize
                )

            updateState {
                copy(
                    isLoading = false,
                    scheduledGames = sortedSchedules,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}