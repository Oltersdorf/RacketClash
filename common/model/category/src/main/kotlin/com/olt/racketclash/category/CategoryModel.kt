package com.olt.racketclash.category

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class CategoryModel(
    private val database: Database,
    private val categoryId: Long
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        updateGamesState()
    }

    fun updatePage(pageNumber: Int) =
        updateGamesState(currentPage = pageNumber)

    private fun updateGamesState(
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val (totalSize, sortedGames) =
                database.game.selectGames(
                    categoryId = categoryId,
                    fromIndex = (currentPage - 1) * pageSize,
                    toIndex = currentPage * pageSize
                )

            updateState {
                copy(
                    isLoading = false,
                    games = sortedGames,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}