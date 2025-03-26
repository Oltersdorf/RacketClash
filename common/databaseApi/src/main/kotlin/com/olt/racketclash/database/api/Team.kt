package com.olt.racketclash.database.api

data class Team(
    val id: Long = -1L,
    val name: String = "",
    val rank: Int = 1,
    val tournamentId: Long = -1L,
    val size: Long = 0L,
    val gamesWon: Long = 0L,
    val gamesDraw: Long = 0L,
    val gamesLost: Long = 0L,
    val gamePointsWon: Long = 0L,
    val gamePointsLost: Long = 0L
)

data class TeamFilter(
    val tournamentId: Long = -1,
    val name: String = ""
)

sealed interface TeamSorting {
    data object NameAsc : TeamSorting
    data object NameDesc : TeamSorting
    data object RankAsc : TeamSorting
    data object RankDesc : TeamSorting
    data object SizeAsc : TeamSorting
    data object SizeDesc : TeamSorting
    data object PointsAsc : TeamSorting
    data object PointsDesc : TeamSorting
}

interface TeamDatabase {

    suspend fun selectList(
        filter: TeamFilter,
        sorting: TeamSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Team, TeamFilter, TeamSorting>

    suspend fun selectSingle(id: Long): Team

    suspend fun add(team: Team, playerIds: Set<Long>)

    suspend fun update(team: Team, playerIds: Set<Long>)

    suspend fun delete(id: Long)
}