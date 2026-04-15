package dev.oltersdorf.racketclash.server.routing

import dev.oltersdorf.racketclash.database.api.FilteredSortedList
import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.database.api.TableBase

abstract class TableBaseImpl<Item: IdItem, Filter, Sorting>(
    initialStore: List<Item> = emptyList()
) : TableBase<Item, Filter, Sorting> {
    val store: MutableList<Item> = initialStore.toMutableList()

    override suspend fun insert(data: Item): Long {
        store += data
        return 0L
    }

    override suspend fun delete(id: Long): Boolean {
        val data = store.find { it.id == id }
        if (data != null) {
            store -= data
            return true
        }
        return false
    }

    override suspend fun update(data: Item): Boolean {
        val index = store.indexOfFirst { it.id == data.id }
        return if (index != -1) {
            store[index] = data
            true
        } else {
            false
        }
    }

    override suspend fun selectSingle(id: Long): Item? =
        store.find { it.id == id }

    override suspend fun selectLast(n: Int): List<Item> =
        store.takeLast(n)

    override suspend fun selectSortedAndFiltered(
        filter: Filter,
        sorting: Sorting,
        fromIndex: Long,
        limit: Int
    ): FilteredSortedList<Item, Filter, Sorting> = FilteredSortedList(
        totalItemsInDatabase = store.size.toLong(),
        fromIndex = fromIndex,
        limit = limit,
        items = store,
        filter = filter,
        sorting = sorting
    )
}