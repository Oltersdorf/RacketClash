package com.olt.racketclash.schedule

import com.olt.racketclash.database.api.GameSet
import com.olt.racketclash.database.api.ScheduleDatabase
import com.olt.racketclash.database.api.ScheduleFilter
import com.olt.racketclash.database.api.ScheduleSorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class ScheduleModel(
    private val database: ScheduleDatabase,
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

    fun onSort(sorting: ScheduleSorting) =
        updateScheduleState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateScheduleState(currentPage = pageNumber)

    fun onSaveResult(
        scheduledGameId: Long,
        sets: List<GameSet>
    ) =
        onIO {
            val scheduledGame = state.value.scheduledGames.find { it.id == scheduledGameId }
            updateState { copy(scheduledGames = scheduledGames.toMutableList().apply { remove(scheduledGame) }) }
            scheduledGame?.let { database.setComplete(schedule = it, sets = sets) }
        }

    private fun updateScheduleState(
        sorting: ScheduleSorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val schedules = database.selectList(
                filter = ScheduleFilter(
                    tournamentId = tournamentId,
                    categoryName = filters.category ?: "",
                    isSingle = filters.singles,
                    isActive = filters.active,
                    playerName = filters.player ?: ""
                ),
                sorting = sorting,
                fromIndex = (currentPage - 1) * pageSize.toLong(),
                toIndex = currentPage * pageSize.toLong()
            )

            updateState {
                copy(
                    isLoading = false,
                    scheduledGames = schedules.items,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((schedules.totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}