package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.database.api.Game
import com.olt.racketclash.database.api.GameDatabase
import kotlin.math.min

internal class GameDatabaseImpl : GameDatabase {
    private val games = mutableListOf<Game>()

    override suspend fun selectList(
        categoryId: Long,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Game, *, *> =
        FilteredSortedList(
            totalSize = games.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = games.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), games.size)),
            filter = object {},
            sorting = object {}
        )

    override suspend fun selectLast(n: Long): List<Game> =
        games.takeLast(n.toInt())
}