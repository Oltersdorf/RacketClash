package com.olt.racketclash.state.list

import com.olt.racketclash.state.ViewModelState
import java.lang.Long.min

abstract class ListModel<Item, Sorting, Filter>(
    initialSorting: Sorting,
    private val filter: () -> Filter
) : ViewModelState<ListState<Item, Sorting>>(initialState = ListState(sorting = initialSorting)) {

    init { updateList() }

    fun deleteItem(item: Item) {
        onIO {
            updateState { copy(items = items - item) }
            databaseDelete(item = item)
        }
    }

    fun sort(sorting: Sorting) =
        updateList(updatedState = state.value.copy(sorting = sorting))

    fun selectPage(pageNumber: Int) =
        updateList(updatedPage = pageNumber)

    internal fun updateList(
        updatedState: ListState<Item, Sorting> = state.value,
        updatedPage: Int = 1,
        filter: Filter = filter()
    ) {
        onIO {
            updateState { updatedState.copy(isLoading = true) }

            val (totalSize, newList) = databaseSelect(
                sorting = updatedState.sorting,
                filter = filter,
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

    protected abstract fun databaseDelete(item: Item)

    protected abstract fun databaseSelect(
        sorting: Sorting,
        filter: Filter?,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<Item>>
}