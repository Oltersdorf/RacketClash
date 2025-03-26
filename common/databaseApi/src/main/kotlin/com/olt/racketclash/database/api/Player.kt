package com.olt.racketclash.database.api

data class PlayerFilter(
    val name: String = "",
    val birthYear: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
    val club: String = "",
    val medals: LongRange = 0L..Long.MAX_VALUE
)

sealed interface PlayerSorting {
    data object NameAsc : PlayerSorting
    data object NameDesc : PlayerSorting
    data object BirthYearAsc : PlayerSorting
    data object BirthYearDesc : PlayerSorting
    data object ClubAsc : PlayerSorting
    data object ClubDesc : PlayerSorting
    data object TournamentsAsc : PlayerSorting
    data object TournamentsDesc : PlayerSorting
    data object MedalsAsc : PlayerSorting
    data object MedalsDesc : PlayerSorting
    data object SinglesAsc : PlayerSorting
    data object SinglesDesc : PlayerSorting
    data object DoublesAsc : PlayerSorting
    data object DoublesDesc : PlayerSorting
}

data class Player(
    val id: Long = -1,
    val name: String = "",
    val birthYear: Int = 1900,
    val club: String = "",
    val numberOfTournaments: Long = 0,
    val goldMedals: Long = 0,
    val silverMedals: Long = 0,
    val bronzeMedals: Long = 0,
    val gamesPlayed: Long = 0,
    val gamesScheduled: Long = 0,
)

interface PlayerDatabase {
    suspend fun selectList(
        filter: PlayerFilter,
        sorting: PlayerSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Player, PlayerFilter, PlayerSorting>

    suspend fun selectLast(n: Long): List<Player>

    suspend fun selectSingle(id: Long): Player

    suspend fun add(player: Player)

    suspend fun update(player: Player)

    suspend fun delete(id: Long)

    suspend fun clubs(filter: String): List<String>
}