package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.database.api.Game
import com.olt.racketclash.database.api.GameDatabase
import com.olt.racketclash.database.toGame
import com.olt.racketclash.database.toGameSet

internal class GameDatabaseImpl(
    private val database: RacketClashDatabase
) : GameDatabase {

    override suspend fun selectList(
        categoryId: Long,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Game, *, *> =
        database.transactionWithResult {
            FilteredSortedList(
                totalSize = database
                    .gameScheduleQueries
                    .selectGameScheduleSize(categoryId = categoryId)
                    .executeAsOne(),
                fromIndex = fromIndex,
                toIndex = toIndex,
                items = database
                    .gameScheduleQueries
                    .selectGameSchedule(
                        categoryId = categoryId,
                        offset = fromIndex,
                        limit = toIndex
                    ).executeAsList()
                    .map { game ->
                        game.toGame(
                            sets = game
                                .gameId
                                ?.let { gameId ->
                                    database
                                        .setQueries
                                        .select(gameId = gameId)
                                        .executeAsList()
                                        .map { it.toGameSet() }
                                } ?: emptyList()
                        )
                    },
                filter = object {},
                sorting = object {}
            )
        }

    override suspend fun selectLast(n: Long): List<Game> =
        database
            .gameQueries
            .selectLast(n = n)
            .executeAsList()
            .map { it.toGame() }
}