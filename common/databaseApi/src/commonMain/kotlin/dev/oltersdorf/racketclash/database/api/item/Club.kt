package dev.oltersdorf.racketclash.database.api.item

import dev.oltersdorf.racketclash.database.api.TableBase

data class Club(
    val id: Long = -1,
    val name: String = ""
)

data class ClubFilter(
    val name: String = ""
)

sealed interface ClubSorting {
    data object NameAsc : ClubSorting
    data object NameDesc : ClubSorting
}

interface ClubTable : TableBase<Club, ClubFilter, ClubSorting>