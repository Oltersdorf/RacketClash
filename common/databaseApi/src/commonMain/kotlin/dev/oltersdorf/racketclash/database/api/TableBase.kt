package dev.oltersdorf.racketclash.database.api

interface TableBase<Item: IdItem, Filter, Sorting> {
    suspend fun insert(data: Item): Long

    suspend fun delete(id: Long): Boolean

    suspend fun update(data: Item): Boolean

    suspend fun selectSingle(id: Long): Item?

    suspend fun selectLast(n: Int): List<Item>

    suspend fun selectSortedAndFiltered(
        filter: Filter,
        sorting: Sorting,
        fromIndex: Long,
        limit: Int
    ): FilteredSortedList<Item, Filter, Sorting>
}