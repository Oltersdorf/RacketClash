package com.olt.racketclash.state.list

import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.state.ViewModelState
import java.lang.Long.min

abstract class ListModel<Item, Filter, Sorting>(
    initialFilter: Filter,
    initialSorting: Sorting,
) : ViewModelState<ListState<Item, Filter, Sorting>>(
        initialState = ListState(
            filter = initialFilter,
            sorting = initialSorting
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
        updatedState: ListState<Item, Filter, Sorting> = state.value,
        updatedPage: Int = 1
    ) {
        onIO {
            updateState { updatedState.copy(isLoading = true) }

            val itemList = databaseSelect(
                sorting = updatedState.sorting,
                filter = updatedState.filter,
                fromIndex = (updatedPage - 1) * updatedState.maxSize.toLong(),
                toIndex = updatedPage * updatedState.maxSize.toLong()
            )

            updateState {
                copy(
                    isLoading = false,
                    items = itemList.items,
                    filter = itemList.filter,
                    sorting = itemList.sorting,
                    currentPage = updatedPage,
                    lastPage = min((itemList.totalSize / (maxSize + 1)) + 1, Int.MAX_VALUE.toLong()).toInt()
                )
            }
        }
    }

    protected abstract suspend fun databaseAdd(item: Item)

    protected abstract suspend fun databaseDelete(item: Item)

    protected abstract suspend fun databaseSelect(
        filter: Filter,
        sorting: Sorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Item, Filter, Sorting>
}