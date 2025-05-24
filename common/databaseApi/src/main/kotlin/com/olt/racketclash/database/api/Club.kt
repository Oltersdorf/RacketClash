package com.olt.racketclash.database.api

data class ClubFilter(
    val name: String = ""
)

sealed interface ClubSorting {
    data object NameAsc : ClubSorting
    data object NameDesc : ClubSorting
}

data class Club(
    val id: Long = -1,
    val name: String = "",
    val players: Long = 0
): Validateable {

    override fun validate(): Boolean {
        return name.isNotBlank()
    }
}

interface ClubDatabase {
    suspend fun selectList(
        filter: ClubFilter,
        sorting: ClubSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Club, ClubFilter, ClubSorting>

    suspend fun selectLast(n: Long): List<Club>

    suspend fun selectSingle(id: Long): Club

    suspend fun add(club: Club)

    suspend fun update(club: Club)

    suspend fun delete(id: Long)
}