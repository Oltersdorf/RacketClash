package com.olt.racketclash.database.api

import java.time.Instant

data class TournamentFilter(
    val name: String = "",
    val location: String = "",
    val start: Instant = Instant.MIN,
    val end: Instant = Instant.MAX
)

sealed interface TournamentSorting {
    data object NameAsc : TournamentSorting
    data object NameDesc : TournamentSorting
    data object LocationAsc : TournamentSorting
    data object LocationDesc : TournamentSorting
    data object CourtsAsc : TournamentSorting
    data object CourtsDesc : TournamentSorting
    data object StartAsc : TournamentSorting
    data object StartDesc : TournamentSorting
    data object EndAsc : TournamentSorting
    data object EndDesc : TournamentSorting
    data object PlayersAsc : TournamentSorting
    data object PlayersDesc : TournamentSorting
}

data class Tournament(
    val id: Long = -1,
    val name: String = "",
    val numberOfCourts: Int = 1,
    val location: String = "",
    val start: Instant = Instant.EPOCH,
    val end: Instant = Instant.EPOCH,
    val playersCount: Long = 0,
    val categoriesCount: Long = 0
)

interface TournamentDatabase {
    suspend fun selectList(
        filter: TournamentFilter,
        sorting: TournamentSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Tournament, TournamentFilter, TournamentSorting>

    suspend fun selectLast(n: Long): List<Tournament>

    suspend fun selectSingle(id: Long): Tournament

    suspend fun add(tournament: Tournament)

    suspend fun update(tournament: Tournament)

    suspend fun delete(id: Long)

    suspend fun locations(filter: String): List<String>
}