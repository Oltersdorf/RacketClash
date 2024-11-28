package com.olt.racketclash.database.category

import com.olt.racketclash.database.RacketClashDatabase

class CategoryDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        tournamentId: Long,
        nameFilter: String,
        finishedFilter: Boolean?,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<DeletableCategory>> =
        database.categoryQueries.selectFilteredAndOrderedSize(
            tournamentId = tournamentId,
            nameFilter = nameFilter,
            finishedFilter = finishedFilter?.let { if (it) 1L else 0L }
        ).executeAsOne().size to
        database.categoryQueries.selectFilteredAndOrdered(
            tournamentId = tournamentId,
            nameFilter = nameFilter,
            finishedFilter = finishedFilter?.let { if (it) 1L else 0L },
            sorting = sorting.name,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList().map { it.toDeletableCategory() }


    fun selectSingle(id: Long): DeletableCategory =
        database
            .categoryQueries
            .selectSingle(id = id)
            .executeAsOne()
            .toDeletableCategory()

    fun add(category: DeletableCategory) =
        database.categoryQueries.add(name = category.name, type = category.type, category.tournamentId)

    fun update(category: DeletableCategory) =
        database.categoryQueries.update(
            id = category.id,
            name = category.name
        )

    fun delete(id: Long) =
        database.categoryQueries.delete(id = id)
}