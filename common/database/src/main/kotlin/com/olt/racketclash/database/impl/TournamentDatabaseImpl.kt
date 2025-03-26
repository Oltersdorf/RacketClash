package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.toName
import com.olt.racketclash.database.toTournament

internal class TournamentDatabaseImpl(
    private val database: RacketClashDatabase
) : TournamentDatabase {

    override suspend fun selectList(
        filter: TournamentFilter,
        sorting: TournamentSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Tournament, TournamentFilter, TournamentSorting> =
        database.transactionWithResult {
            FilteredSortedList(
                totalSize = database
                    .tournamentQueries
                    .selectFilteredAndOrderedSize(
                        nameFilter = filter.name,
                        locationFilter = filter.location,
                        startFilter = filter.start.epochSecond,
                        endFilter = filter.end.epochSecond
                    ).executeAsOne(),
                fromIndex = fromIndex,
                toIndex = toIndex,
                items = database
                    .tournamentQueries
                    .selectFilteredAndOrdered(
                        nameFilter = filter.name,
                        locationFilter = filter.location,
                        startFilter = filter.start.epochSecond,
                        endFilter = filter.end.epochSecond,
                        sorting = sorting.toName(),
                        offset = fromIndex,
                        limit = toIndex
                    ).executeAsList()
                    .map { it.toTournament() },
                filter = filter,
                sorting = sorting
            )
        }

    override suspend fun selectLast(n: Long): List<Tournament> =
        database
            .tournamentQueries
            .selectLast(n = n)
            .executeAsList()
            .map { it.toTournament() }

    override suspend fun selectSingle(id: Long): Tournament =
        database
            .tournamentQueries
            .selectSingle(id = id)
            .executeAsOne()
            .toTournament()

    override suspend fun locations(filter: String): List<String> =
        database
            .tournamentQueries
            .locations(locationFilter = filter)
            .executeAsList()

    override suspend fun add(tournament: Tournament) =
        database.tournamentQueries.add(
            name = tournament.name,
            numberOfCourts = tournament.numberOfCourts,
            location = tournament.location,
            startTime = tournament.start.epochSecond,
            endTime = tournament.end.epochSecond
        )

    override suspend fun update(tournament: Tournament) =
        database.tournamentQueries.update(
            id = tournament.id,
            name = tournament.name,
            numberOfCourts = tournament.numberOfCourts,
            location = tournament.location,
            startTime = tournament.start.epochSecond,
            endTime = tournament.end.epochSecond
        )

    override suspend fun delete(id: Long) =
        database.tournamentQueries.delete(id = id)
}