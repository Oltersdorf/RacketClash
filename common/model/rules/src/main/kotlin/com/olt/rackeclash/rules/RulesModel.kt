package com.olt.rackeclash.rules

import com.olt.racketclash.database.Database
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

        onDefault {
            updateState {
                val tags = availableTags.toMutableList()
                tags.replaceAll { it.changeText(newText = text) }

                copy(availableTags = tags.toList())
            }
        }
    }

    fun addTag(tag: Tag) {
        updateState { copy(availableTags = availableTags - tag, tags = tags + tag) }

        updateRulesState()
    }

    fun removeTag(tag: Tag) {
        updateState {
            copy(
                tags = tags - tag,
                availableTags = availableTags + tag.changeText(newText = searchBarText)
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

            val nameFilter = state.value.tags.filterIsInstance<Tag.Name>().firstOrNull()?.text ?: ""

            val (totalSize, sortedRules) = when (sorting) {
                Sorting.NameAsc -> database.rules.selectFilteredAndOrderedByNameAsc(
                    nameFilter = nameFilter,
                    fromIndex = (currentPage - 1) * pageSize,
                    toIndex = currentPage * pageSize
                )
                Sorting.NameDesc -> database.rules.selectFilteredAndOrderedByNameDesc(
                    nameFilter = nameFilter,
                    fromIndex = (currentPage - 1) * pageSize,
                    toIndex = currentPage * pageSize
                )
            }

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