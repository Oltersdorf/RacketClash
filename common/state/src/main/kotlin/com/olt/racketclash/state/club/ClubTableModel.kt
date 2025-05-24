package com.olt.racketclash.state.club

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListModel

class ClubTableModel(
    private val database: ClubDatabase
) : ListModel<Club, ClubFilter, ClubSorting>(
    emptyItem = Club(),
    initialFilter = ClubFilter(),
    initialSorting = ClubSorting.NameAsc
) {
    override suspend fun databaseDelete(item: Club) =
        database.delete(id = item.id)

    override suspend fun databaseSelect(
        filter: ClubFilter,
        sorting: ClubSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Club, ClubFilter, ClubSorting> =
        database.selectList(
            filter = filter,
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override suspend fun databaseAdd(item: Club) =
        database.add(club = item)
}