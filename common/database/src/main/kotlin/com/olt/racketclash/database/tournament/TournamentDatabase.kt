package com.olt.racketclash.database.tournament

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.DateTimeConverter

class TournamentDatabase internal constructor(
    private val database: RacketClashDatabase,
    private val dateTimeConverter: DateTimeConverter
) {
    constructor(
        database: RacketClashDatabase
    ) : this(
        database = database,
        dateTimeConverter = DateTimeConverter()
    )

    fun selectFilteredAndOrdered(
        nameFilter: String,
        locationFilter: String,
        yearFilter: Int?,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<DeletableTournament>> =
        database.tournamentQueries.selectFilteredAndOrderedSize(
            nameFilter = nameFilter,
            locationFilter = locationFilter,
            yearStartFilter = yearFilter?.let { dateTimeConverter.yearStart(year = it) },
            yearEndFilter = yearFilter?.let { dateTimeConverter.yearEnd(year = it) }
        ).executeAsOne() to
        database.tournamentQueries.selectFilteredAndOrdered(
            nameFilter = nameFilter,
            locationFilter = locationFilter,
            yearStartFilter = yearFilter?.let { dateTimeConverter.yearStart(year = it) },
            yearEndFilter = yearFilter?.let { dateTimeConverter.yearEnd(year = it) },
            sorting = sorting.name,
            limit = toIndex.toLong(),
            offset = fromIndex.toLong()
        ).executeAsList().map { it.toDeletableTournament(dateTimeConverter = dateTimeConverter) }

    fun selectSingle(id: Long): DeletableTournament =
        database.tournamentQueries
            .selectSingle(id = id)
            .executeAsOne()
            .toDeletableTournament(dateTimeConverter = dateTimeConverter)

    fun locations(filter: String): List<String> =
        database.tournamentQueries.locations(locationFilter = filter).executeAsList()

    fun add(tournament: DeletableTournament) =
        database.tournamentQueries.add(
            name = tournament.name,
            numberOfCourts = tournament.numberOfCourts,
            location = tournament.location,
            unixStartDateTime = dateTimeConverter.toLong(dateTime = tournament.startDateTime),
            unixEndDateTime = dateTimeConverter.toLong(dateTime = tournament.endDateTime),
            zoneId = dateTimeConverter.defaultZoneId
        )

    fun update(tournament: DeletableTournament) =
        database.tournamentQueries.update(
            id = tournament.id,
            name = tournament.name,
            numberOfCourts = tournament.numberOfCourts,
            location = tournament.location,
            unixStartDateTime = dateTimeConverter.toLong(dateTime = tournament.startDateTime),
            unixEndDateTime = dateTimeConverter.toLong(dateTime = tournament.endDateTime),
            zoneId = dateTimeConverter.defaultZoneId
        )

    fun delete(id: Long) =
        database.tournamentQueries.delete(id = id)
}