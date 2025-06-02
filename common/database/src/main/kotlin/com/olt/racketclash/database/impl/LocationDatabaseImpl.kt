package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.*
import kotlin.math.min

internal class LocationDatabaseImpl : LocationDatabase {
    private val locations = mutableListOf<Location>()

    override suspend fun selectList(
        filter: LocationFilter,
        sorting: LocationSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Location, LocationFilter, LocationSorting> =
        FilteredSortedList(
            totalSize = locations.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = locations.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), locations.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectLast(n: Long): List<Location> =
        locations.takeLast(n.toInt())

    override suspend fun selectSingle(id: Long): Location =
        locations.first { it.id == id }

    override suspend fun add(location: Location) {
        locations.add(location)
    }

    override suspend fun update(location: Location) {
        locations.replaceAll {
            if (it.id == location.id)
                location
            else
                it
        }
    }

    override suspend fun delete(id: Long) {
        locations.removeIf { it.id == id }
    }

}