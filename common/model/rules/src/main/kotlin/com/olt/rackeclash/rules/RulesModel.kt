package com.olt.rackeclash.rules

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.state.ViewModelState
import kotlin.math.min

class RulesModel(
    private val database: Database
) : ViewModelState<State>(initialState = State()) {

    private val pageSize = 50

    init {
        updateRulesState()
    }

    fun updateSearchBar(text: String) {
        updateState { copy(searchBarText = text) }

        updateState {
            copy(availableTags = Tags(name = if (tags.name == null) text else null))
        }
    }

    fun addNameTag() {
        updateState {
            copy(
                availableTags = availableTags.copy(name = null),
                tags = tags.copy(name = availableTags.name)
            )
        }

        updateRulesState()
    }

    fun removeNameTag() {
        updateState {
            copy(
                tags = tags.copy(name = null),
                availableTags = availableTags.copy(name = searchBarText)
            )
        }

        updateRulesState()
    }

    fun onSort(sorting: Sorting) =
        updateRulesState(sorting = sorting)

    fun updatePage(pageNumber: Int) =
        updateRulesState(currentPage = pageNumber)

    fun deleteRule(id: Long) {
        onIO {
            updateState { copy(rules = rules.toMutableList().apply { removeIf { it.id == id } }) }
            database.rules.delete(id = id)
        }
    }

    private fun updateRulesState(
        sorting: Sorting = state.value.sorting,
        currentPage: Int = 1
    ) =
        onIO {
            updateState { copy(isLoading = true) }

            val filters = state.value.tags

            val (totalSize, sortedRules) = database.rules.selectFilteredAndOrdered(
                nameFilter = filters.name ?: "",
                sorting = sorting,
                fromIndex = (currentPage - 1) * pageSize,
                toIndex = currentPage * pageSize
            )

            updateState {
                copy(
                    isLoading = false,
                    rules = sortedRules,
                    sorting = sorting,
                    currentPage = currentPage,
                    lastPage = min((totalSize / (pageSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
    }