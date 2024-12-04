package com.olt.racketclash.database.schedule

import com.olt.racketclash.database.DateTimeConverter
import com.olt.racketclash.database.RacketClashDatabase
import java.time.Instant

class ScheduleDatabase(private val database: RacketClashDatabase) {

    private val dateTimeConverter = DateTimeConverter()

    fun selectFilteredAndOrdered(
        tournamentId: Long,
        categoryNameFilter: String,
        isSingleFilter: Boolean?,
        isActiveFilter: Boolean?,
        playerNameFilter: String,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<Schedule>> =
        database.scheduleQueries.selectFilteredAndOrderedSize(
            tournamentId = tournamentId,
            categoryNameFilter = categoryNameFilter,
            isSingleFilter = isSingleFilter?.let { if (it) 1L else 0L },
            isActiveFilter = isActiveFilter,
            playerNameFilter = playerNameFilter
        ).executeAsOne() to
        database.scheduleQueries.selectFilteredAndOrdered(
            tournamentId = tournamentId,
            categoryNameFilter = categoryNameFilter,
            isSingleFilter = isSingleFilter?.let { if (it) 1L else 0L },
            isActiveFilter = isActiveFilter,
            playerNameFilter = playerNameFilter,
            sorting = sorting.name,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList().map { it.toSchedule(dateTimeConverter = dateTimeConverter) }

    fun setComplete(
        scheduledGame: Schedule,
        sets: List<Pair<Int, Int>>
    ) = database.transaction {
        database.scheduleQueries.delete(id = scheduledGame.id)
        database.gameQueries.add(
            ruleId = scheduledGame.ruleId,
            isRest = false,
            unixTimeScheduled = dateTimeConverter.toLong(dateTime = scheduledGame.scheduledFor),
            unixTimeSubmitted = Instant.now().epochSecond,
            zoneId = dateTimeConverter.defaultZoneId,
            categoryId = scheduledGame.categoryId,
            categoryOrderNumber = scheduledGame.categoryOrderNumber,
            playerIdLeftOne = scheduledGame.playerIdLeftOne,
            playerIdLeftTwo = scheduledGame.playerIdLeftTwo,
            playerIdRightOne = scheduledGame.playerIdRightOne,
            playerIdRightTwo = scheduledGame.playerIdRightTwo
        )
        val lastInsertedGameId = database.gameQueries.lastInsertedId().executeAsOne()
        sets.forEachIndexed { index, set ->
            database.setQueries.add(
                gameId = lastInsertedGameId,
                orderNumber = index,
                leftPoints = set.first,
                rightPoints = set.second
            )
        }
    }

    fun add(
        categoryId: Long,
        categoryOrderNumber: Int,
        playerIdLeftOne: Long,
        playerIdLeftTwo: Long?,
        playerIdRightOne: Long,
        playerIdRightTwo: Long?,
        scheduledFor: Long
    ) =
        database.transaction {
            val category = database.categoryQueries.selectSingle(id = categoryId).executeAsOne()
            val tournament = database.tournamentQueries.selectSingle(id = category.tournamentId).executeAsOne()
            val activeGames = database.scheduleQueries.active(categoryId = categoryId).executeAsOne()

            database.scheduleQueries.add(
                ruleId = 1L,
                unixTimeScheduled = scheduledFor,
                zoneId = dateTimeConverter.defaultZoneId,
                active = activeGames < tournament.numberOfCourts.toLong(),
                categoryId = categoryId,
                categoryOrderNumber = categoryOrderNumber,
                playerIdLeftOne = playerIdLeftOne,
                playerIdLeftTwo = playerIdLeftTwo,
                playerIdRightOne = playerIdRightOne,
                playerIdRightTwo = playerIdRightTwo
            )
        }

}