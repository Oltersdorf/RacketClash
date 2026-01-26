package dev.oltersdorf.racketclash.database.api

interface TableBase<Item, Filter, Sorting> {
    suspend fun insert(data: Item)

    suspend fun delete(id: Long)

    suspend fun update(data: Item)

    suspend fun selectSingle(id: Long): Item?

    suspend fun selectLast(n: Long): List<Item>

    suspend fun selectSortedAndFiltered(
        filter: Filter,
        sorting: Sorting,
        fromIndex: Long,
        limit: Int
    ): FilteredSortedList<Item, Filter, Sorting>
}