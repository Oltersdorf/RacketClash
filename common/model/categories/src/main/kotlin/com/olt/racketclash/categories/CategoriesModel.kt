package com.olt.racketclash.categories

import com.olt.racketclash.database.api.CategoryDatabase
import com.olt.racketclash.database.api.CategoryFilter
import com.olt.racketclash.database.api.CategorySorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class CategoriesModel(
    private val database: CategoryDatabase,
    private val tournamentId: Long
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        updateCategoriesState()
    }

    fun updateSearchBar(text: String) {
        updateState { copy(searchBarText = text) }

        onDefault {
            updateState {
                copy(availableTags = Tags(name = if (tags.name == null) text else null))
            }
        }
    }

    fun addNameTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(name = null),
                tags = tags.copy(name = availableTags.name)
            )
        }

        updateCategoriesState()
    }

    fun removeNameTag() {
        updateState {
            copy(
                tags = tags.copy(name = null),
                availableTags = availableTags.copy(name = searchBarText)
            )
        }

        updateCategoriesState()
    }

    fun addFinishedTag(value: Boolean) {
        updateState {
            copy(
                availableTags = availableTags.copy(finished = null),
                tags = tags.copy(finished = value)
            )
        }

        updateCategoriesState()
    }

    fun removeFinishedTag() {
        updateState {
            copy(
                tags = tags.copy(finished = null),
                availableTags = availableTags.copy(finished = true)
            )
        }

        updateCategoriesState()
    }

    fun onSort(sorting: CategorySorting) =
        updateCategoriesState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateCategoriesState(currentPage = pageNumber)

    fun deleteCategory(id: Long) {
        onIO {
            updateState { copy(categories = categories.toMutableList().apply { removeIf { it.id == id } }) }
            database.delete(id = id)
        }
    }

    private fun updateCategoriesState(
        sorting: CategorySorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val categories =
                database.selectList(
                    filter = CategoryFilter(
                        tournamentId = tournamentId,
                        name = filters.name ?: "",
                        minOpenGames = when (filters.finished) {
                            true -> 0L
                            false -> 1L
                            null -> 0L
                        },
                        maxOpenGames = when (filters.finished) {
                            true -> 0L
                            false -> Long.MAX_VALUE
                            null -> Long.MAX_VALUE
                        }
                    ),
                    sorting = sorting,
                    fromIndex = (currentPage - 1) * pageSize.toLong(),
                    toIndex = currentPage * pageSize.toLong()
                )

            updateState {
                copy(
                    isLoading = false,
                    categories = categories.items,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((categories.totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}