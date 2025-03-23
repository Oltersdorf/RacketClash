package com.olt.racketclash.state.list

import com.olt.racketclash.state.ViewModelState
import java.lang.Long.min

abstract class ListModel<Item, Sorting, Filter>(
    initialSorting: Sorting,
    initialFilter: Filter
) : ViewModelState<ListState<Item, Sorting, Filter>>(
        initialState = ListState(
            sorting = initialSorting,
            filter = initialFilter
        )
) {

    init { updateList() }

    fun add(item: Item) {
        onIO {
            databaseAdd(item = item)
            updateList(updatedPage = state.value.currentPage)
        }
    }

    fun delete(item: Item) {
        onIO {
            databaseDelete(item = item)

            val currentPage = state.value.currentPage
            val targetPage =
                if (state.value.items.size == 1 && currentPage != 1)
                    currentPage - 1
                else
                    currentPage

            updateList(updatedPage = targetPage)
        }
    }

    fun sort(sorting: Sorting) =
        updateList(updatedState = state.value.copy(sorting = sorting))

    fun filter(filter: Filter) =
        updateList(updatedState = state.value.copy(filter = filter))

    fun selectPage(pageNumber: Int) =
        updateList(updatedPage = pageNumber)

    internal fun updateList(
        updatedState: ListState<Item, Sorting, Filter> = state.value,
        updatedPage: Int = 1
    ) {
        onIO {
            updateState { updatedState.copy(isLoading = true) }

            val (totalSize, newList) = databaseSelect(
                sorting = updatedState.sorting,
                filter = updatedState.filter,
                fromIndex = (updatedPage - 1) * updatedState.maxSize,
                toIndex = updatedPage * updatedState.maxSize
            )

            updateState {
                copy(
                    isLoading = false,
                    items = newList,
                    currentPage = updatedPage,
                    lastPage = min((totalSize / (maxSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
    }

    protected abstract fun databaseAdd(item: Item)

    protected abstract fun databaseDelete(item: Item)

    protected abstract fun databaseSelect(
        sorting: Sorting,
        filter: Filter?,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<Item>>
}