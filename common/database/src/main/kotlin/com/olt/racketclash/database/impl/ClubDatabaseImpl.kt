package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.Club
import com.olt.racketclash.database.api.ClubDatabase
import com.olt.racketclash.database.api.ClubFilter
import com.olt.racketclash.database.api.ClubSorting
import com.olt.racketclash.database.api.FilteredSortedList
import kotlin.math.min

class ClubDatabaseImpl : ClubDatabase {
    private val clubs = mutableListOf<Club>()

    override suspend fun selectList(
        filter: ClubFilter,
        sorting: ClubSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Club, ClubFilter, ClubSorting> =
        FilteredSortedList(
            totalSize = clubs.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = clubs.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), clubs.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectLast(n: Long): List<Club> =
        clubs.takeLast(n.toInt())

    override suspend fun selectSingle(id: Long): Club =
        clubs.first { it.id == id }

    override suspend fun add(club: Club) {
        clubs.add(club)
    }

    override suspend fun update(club: Club) {
        clubs.replaceAll {
            if (it.id == club.id)
                club
            else
                it
        }
    }

    override suspend fun delete(id: Long) {
        clubs.removeIf { it.id == id }
    }
}