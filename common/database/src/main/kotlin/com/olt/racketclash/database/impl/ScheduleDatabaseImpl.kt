package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.toName
import com.olt.racketclash.database.toSchedule
import java.time.Instant

internal class ScheduleDatabaseImpl(
    private val database: RacketClashDatabase
) : ScheduleDatabase {

    override suspend fun selectList(
        filter: ScheduleFilter,
        sorting: ScheduleSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Schedule, ScheduleFilter, ScheduleSorting> =
        database.transactionWithResult {
            FilteredSortedList(
                totalSize = database
                    .scheduleQueries
                    .selectFilteredAndOrderedSize(
                        tournamentId = filter.tournamentId,
                        categoryNameFilter = filter.categoryName,
                        isSingleFilter = filter.isSingle?.let { if (it) 1L else 0L },
                        isActiveFilter = filter.isActive,
                        playerNameFilter = filter.playerName
                    ).executeAsOne(),
                fromIndex = fromIndex,
                toIndex = toIndex,
                items = database
                    .scheduleQueries
                    .selectFilteredAndOrdered(
                        tournamentId = filter.tournamentId,
                        categoryNameFilter = filter.categoryName,
                        isSingleFilter = filter.isSingle?.let { if (it) 1L else 0L },
                        isActiveFilter = filter.isActive,
                        playerNameFilter = filter.playerName,
                        sorting = sorting.toName(),
                        limit = toIndex,
                        offset = fromIndex
                    ).executeAsList()
                    .map { it.toSchedule() },
                filter = filter,
                sorting = sorting
            )
        }

    override suspend fun setComplete(
        schedule: Schedule,
        sets: List<GameSet>
    ) = database.transaction {
        database.scheduleQueries.delete(id = schedule.id)
        database.gameQueries.add(
            ruleId = schedule.ruleId,
            isRest = false,
            unixTimeScheduled = schedule.scheduledFor.epochSecond,
            unixTimeSubmitted = Instant.now().epochSecond,
            categoryId = schedule.categoryId,
            categoryOrderNumber = schedule.categoryOrderNumber,
            tournamentId = schedule.tournamentId,
            playerIdLeftOne = schedule.playerIdLeftOne,
            playerIdLeftTwo = schedule.playerIdLeftTwo,
            playerIdRightOne = schedule.playerIdRightOne,
            playerIdRightTwo = schedule.playerIdRightTwo
        )
        val lastInsertedGameId = database.gameQueries.lastInsertedId().executeAsOne()
        sets.forEach {
            database.setQueries.add(
                gameId = lastInsertedGameId,
                orderNumber = it.orderNumber,
                leftPoints = it.leftPoints,
                rightPoints = it.rightPoints
            )
        }
    }

    override suspend fun add(schedule: Schedule) =
        database.transaction {
            val category = database.categoryQueries.selectSingle(id = schedule.categoryId).executeAsOne()
            val tournament = database.tournamentQueries.selectSingle(id = category.tournamentId).executeAsOne()
            val activeGames = database.scheduleQueries.active(categoryId = schedule.categoryId).executeAsOne()

            database.scheduleQueries.add(
                ruleId = 1L,
                unixTimeScheduled = schedule.scheduledFor.epochSecond,
                active = activeGames < tournament.numberOfCourts.toLong(),
                categoryId = schedule.categoryId,
                categoryOrderNumber = schedule.categoryOrderNumber,
                tournamentId = schedule.tournamentId,
                playerIdLeftOne = schedule.playerIdLeftOne,
                playerIdLeftTwo = schedule.playerIdLeftTwo,
                playerIdRightOne = schedule.playerIdRightOne,
                playerIdRightTwo = schedule.playerIdRightTwo
            )
        }

}