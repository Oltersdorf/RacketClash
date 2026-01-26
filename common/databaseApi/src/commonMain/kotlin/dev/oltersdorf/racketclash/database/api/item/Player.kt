package dev.oltersdorf.racketclash.database.api.item

import dev.oltersdorf.racketclash.database.api.TableBase

data class Player(
    val id: Long = -1,
    val name: String = "",
    val birthYear: Int = 1900,
    val clubId: Long = -1,
    val clubName: String = ""
)

data class PlayerFilter(
    val name: String = "",
    val birthYear: IntRange = 1900..2100,
    val club: String = ""
)

sealed interface PlayerSorting {
    data object NameAsc : PlayerSorting
    data object NameDesc : PlayerSorting
    data object BirthYearAsc : PlayerSorting
    data object BirthYearDesc : PlayerSorting
    data object ClubAsc : PlayerSorting
    data object ClubDesc : PlayerSorting
}

interface PlayerTable : TableBase<Player, PlayerFilter, PlayerSorting> {
    suspend fun clubs(filter: String): List<String>
}