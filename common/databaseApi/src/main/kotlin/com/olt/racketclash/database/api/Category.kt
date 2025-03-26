package com.olt.racketclash.database.api

data class Category(
    val id: Long = -1,
    val name: String = "",
    val type: CategoryType = CategoryType.Custom,
    val tournamentId: Long = -1,
    val players: Int = 0,
    val finished: Boolean = false
)

sealed interface CategoryType {
    data object Custom : CategoryType
    data object Tree : CategoryType
    data object Table : CategoryType
}

data class CategoryFilter(
    val tournamentId: Long = -1,
    val name: String = "",
    val minOpenGames: Long = 0,
    val maxOpenGames: Long = Long.MAX_VALUE
)

sealed interface CategorySorting {
    data object NameAsc : CategorySorting
    data object NameDesc : CategorySorting
    data object TypeAsc : CategorySorting
    data object TypeDesc : CategorySorting
    data object PlayersAsc : CategorySorting
    data object PlayersDesc : CategorySorting
    data object StatusAsc : CategorySorting
    data object StatusDesc : CategorySorting
}

interface CategoryDatabase {

    suspend fun selectList(
        filter: CategoryFilter,
        sorting: CategorySorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Category, CategoryFilter, CategorySorting>

    suspend fun selectSingle(id: Long): Category

    suspend fun add(category: Category)

    suspend fun update(category: Category)

    suspend fun delete(id: Long)
}