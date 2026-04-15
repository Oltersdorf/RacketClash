package dev.oltersdorf.racketclash.database.api.item

import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.database.api.TableBase
import kotlinx.serialization.Serializable

@Serializable
data class Club(
    override val id: Long = -1,
    val name: String = ""
) : IdItem

@Serializable
data class ClubFilter(
    val name: String = ""
)

@Serializable
sealed interface ClubSorting {

    @Serializable data object NameAsc : ClubSorting
    @Serializable data object NameDesc : ClubSorting
}

interface ClubTable : TableBase<Club, ClubFilter, ClubSorting>