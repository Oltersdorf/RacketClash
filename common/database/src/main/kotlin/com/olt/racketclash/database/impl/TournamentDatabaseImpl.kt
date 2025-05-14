package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.database.api.Tournament
import com.olt.racketclash.database.api.TournamentDatabase
import com.olt.racketclash.database.api.TournamentFilter
import com.olt.racketclash.database.api.TournamentSorting
import kotlin.math.min

internal class TournamentDatabaseImpl : TournamentDatabase {
    private val tournaments = mutableListOf<Tournament>()

    override suspend fun selectList(
        filter: TournamentFilter,
        sorting: TournamentSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Tournament, TournamentFilter, TournamentSorting> =
        FilteredSortedList(
            totalSize = tournaments.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = tournaments.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), tournaments.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectLast(n: Long): List<Tournament> =
        tournaments.takeLast(n.toInt())

    override suspend fun selectSingle(id: Long): Tournament =
        tournaments.first { it.id == id }

    override suspend fun locations(filter: String): List<String> =
        tournaments.map { it.location }.toSet().toList()

    override suspend fun add(tournament: Tournament) {
        tournaments.add(tournament)
    }

    override suspend fun update(tournament: Tournament) {
        tournaments.replaceAll {
            if (it.id == tournament.id)
                tournament
            else
                it
        }
    }

    override suspend fun delete(id: Long) {
        tournaments.removeIf { it.id == id }
    }
}