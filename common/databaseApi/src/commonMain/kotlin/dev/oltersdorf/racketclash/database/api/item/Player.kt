package dev.oltersdorf.racketclash.database.api.item

import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.database.api.TableBase
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    override val id: Long = -1,
    val name: String = "",
    val birthYear: Int = 1900,
    val clubId: Long = -1,
    val clubName: String = ""
) : IdItem

@Serializable
data class PlayerFilter(
    val name: String = "",
    val birthYearStart: Int = 1900,
    val birthYearEnd: Int = 2100,
    val club: String = ""
)

@Serializable
sealed interface PlayerSorting {
    @Serializable data object NameAsc : PlayerSorting
    @Serializable data object NameDesc : PlayerSorting
    @Serializable data object BirthYearAsc : PlayerSorting
    @Serializable data object BirthYearDesc : PlayerSorting
    @Serializable data object ClubAsc : PlayerSorting
    @Serializable data object ClubDesc : PlayerSorting
}

interface PlayerTable : TableBase<Player, PlayerFilter, PlayerSorting> {
    suspend fun clubs(filter: String): List<String>
}