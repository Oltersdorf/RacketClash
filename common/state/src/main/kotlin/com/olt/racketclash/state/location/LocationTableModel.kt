package com.olt.racketclash.state.location

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListModel

class LocationTableModel(
    private val database: LocationDatabase
) : ListModel<Location, LocationFilter, LocationSorting>(
    emptyItem = Location(),
    initialFilter = LocationFilter(),
    initialSorting = LocationSorting.NameAsc
) {
    override suspend fun databaseDelete(item: Location) =
        database.delete(id = item.id)

    override suspend fun databaseSelect(
        filter: LocationFilter,
        sorting: LocationSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Location, LocationFilter, LocationSorting> =
        database.selectList(
            filter = filter,
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override suspend fun databaseAdd(item: Location) =
        database.add(location = item)
}