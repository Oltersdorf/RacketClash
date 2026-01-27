package dev.oltersdorf.racketclash.database.api.item

import dev.oltersdorf.racketclash.database.api.IdItem
import dev.oltersdorf.racketclash.database.api.TableBase

data class Club(
    override val id: Long = -1,
    val name: String = ""
) : IdItem

data class ClubFilter(
    val name: String = ""
)

sealed interface ClubSorting {
    data object NameAsc : ClubSorting
    data object NameDesc : ClubSorting
}

interface ClubTable : TableBase<Club, ClubFilter, ClubSorting>