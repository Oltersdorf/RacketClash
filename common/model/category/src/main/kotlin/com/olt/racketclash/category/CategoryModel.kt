package com.olt.racketclash.category

import com.olt.racketclash.database.api.GameDatabase
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class CategoryModel(
    private val database: GameDatabase,
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

            val games = database.selectList(
                categoryId = categoryId,
                fromIndex = (currentPage - 1) * pageSize.toLong(),
                toIndex = currentPage * pageSize.toLong()
            )

            updateState {
                copy(
                    isLoading = false,
                    games = games.items,
                    currentPage = currentPage,
                    lastPage = min((games.totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}