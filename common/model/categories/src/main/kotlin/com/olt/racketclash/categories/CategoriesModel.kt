package com.olt.racketclash.categories

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.category.Sorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class CategoriesModel(
    private val database: Database,
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

    fun onSort(sorting: Sorting) =
        updateCategoriesState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateCategoriesState(currentPage = pageNumber)

    fun deleteCategory(id: Long) {
        onIO {
            updateState { copy(categories = categories.toMutableList().apply { removeIf { it.id == id } }) }
            database.categories.delete(id = id)
        }
    }

    private fun updateCategoriesState(
        sorting: Sorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val (totalSize, sortedCategories) =
                database.categories.selectFilteredAndOrdered(
                    tournamentId = tournamentId,
                    nameFilter = filters.name ?: "",
                    finishedFilter = filters.finished,
                    sorting = sorting,
                    fromIndex = (currentPage - 1) * pageSize,
                    toIndex = currentPage * pageSize
                )

            updateState {
                copy(
                    isLoading = false,
                    categories = sortedCategories,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
}