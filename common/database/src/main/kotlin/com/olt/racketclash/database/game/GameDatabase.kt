package com.olt.racketclash.database.game

import com.olt.racketclash.database.DateTimeConverter
import com.olt.racketclash.database.RacketClashDatabase

class GameDatabase(private val database: RacketClashDatabase) {

    private val dateTimeConverter = DateTimeConverter()

    fun selectGames(
        categoryId: Long,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<ScheduleGame>> =
        database.gameScheduleQueries.selectGameScheduleSize(categoryId = categoryId).executeAsOne() to
        database.gameScheduleQueries.selectGameSchedule(
            categoryId = categoryId,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList().map { gameSchedule ->
            gameSchedule.toScheduleGame(
                dateTimeConverter = dateTimeConverter,
                sets = gameSchedule.gameId?.let { database.setQueries.select(gameId = it).executeAsList() } ?: emptyList()
            )
        }
}