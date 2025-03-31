package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.toCategory
import com.olt.racketclash.database.toName

internal class CategoryDatabaseImpl(
    private val database: RacketClashDatabase
) : CategoryDatabase {

    override suspend fun selectList(
        filter: CategoryFilter,
        sorting: CategorySorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Category, CategoryFilter, CategorySorting> =
        database.transactionWithResult {
            FilteredSortedList(
                totalSize = database
                    .categoryQueries
                    .selectFilteredAndOrderedSize(
                        tournamentId = filter.tournamentId,
                        nameFilter = filter.name,
                        minOpenGames = filter.minOpenGames,
                        maxOpenGames = filter.maxOpenGames
                    ).executeAsOne()
                    .size,
                fromIndex = fromIndex,
                toIndex = toIndex,
                items = database
                    .categoryQueries
                    .selectFilteredAndOrdered(
                        tournamentId = filter.tournamentId,
                        nameFilter = filter.name,
                        minOpenGames = filter.minOpenGames,
                        maxOpenGames = filter.maxOpenGames,
                        sorting = sorting.toName(),
                        offset = fromIndex,
                        limit = toIndex
                    ).executeAsList()
                    .map { it.toCategory() },
                filter = filter,
                sorting = sorting
            )
        }

    override suspend fun selectLast(n: Long): List<Category> =
        database
            .categoryQueries
            .selectLast(n = n)
            .executeAsList()
            .map { it.toCategory() }

    override suspend fun selectSingle(id: Long): Category =
        database
            .categoryQueries
            .selectSingle(id = id)
            .executeAsOne()
            .toCategory()

    override suspend fun add(category: Category) =
        database
            .categoryQueries
            .add(name = category.name, type = category.type, category.tournamentId)

    override suspend fun update(category: Category) =
        database
            .categoryQueries
            .update(id = category.id, name = category.name)

    override suspend fun delete(id: Long) =
        database.categoryQueries.delete(id = id)
}