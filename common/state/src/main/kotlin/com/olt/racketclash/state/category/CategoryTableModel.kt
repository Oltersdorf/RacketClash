package com.olt.racketclash.state.category

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListModel

class CategoryTableModel(
    private val database: CategoryDatabase,
    private val tournamentId: Long
) : ListModel<Category, CategoryFilter, CategorySorting>(
    emptyItem = Category(),
    initialFilter = CategoryFilter(tournamentId = tournamentId),
    initialSorting = CategorySorting.NameAsc
) {
    override suspend fun databaseAdd(item: Category) =
        database.add(category = item.copy(tournamentId = tournamentId))

    override suspend fun databaseSelect(
        filter: CategoryFilter,
        sorting: CategorySorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Category, CategoryFilter, CategorySorting> =
        database.selectList(
            filter = filter,
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override suspend fun databaseDelete(item: Category) =
        database.delete(id = item.id)
}