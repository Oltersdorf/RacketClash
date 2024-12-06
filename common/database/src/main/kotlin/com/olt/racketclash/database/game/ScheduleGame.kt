package com.olt.racketclash.database.game

import com.olt.racketclash.database.DateTimeConverter
import com.olt.racketclash.database.table.GameSet
import com.olt.racketclash.database.view.SelectGameSchedule

data class ScheduleGame(
    val gameId: Long?,
    val scheduleId: Long?,
    val scheduled: String,
    val submitted: String?,
    val playerIdLeftOne: Long,
    val playerNameLeftOne: String,
    val playerTeamIdLeftOne: Long?,
    val playerTeamNameLeftOne: String?,
    val playerIdLeftTwo: Long?,
    val playerNameLeftTwo: String?,
    val playerTeamIdLeftTwo: Long?,
    val playerTeamNameLeftTwo: String?,
    val playerIdRightOne: Long?,
    val playerNameRightOne: String?,
    val playerTeamIdRightOne: Long?,
    val playerTeamNameRightOne: String?,
    val playerIdRightTwo: Long?,
    val playerNameRightTwo: String?,
    val playerTeamIdRightTwo: Long?,
    val playerTeamNameRightTwo: String?,
    val sets: List<GameSet>,
    val leftWon: Boolean?
)

internal fun SelectGameSchedule.toScheduleGame(
    dateTimeConverter: DateTimeConverter,
    sets: List<GameSet>
) =
    ScheduleGame(
        gameId = gameId,
        scheduleId = scheduleId,
        scheduled = dateTimeConverter.toString(unixDateTimeSeconds = unixTimeScheduled, zoneId = zoneId),
        submitted = unixTimeSubmitted?.let { dateTimeConverter.toString(unixDateTimeSeconds = it, zoneId = zoneId) },
        playerIdLeftOne = playerIdLeftOne,
        playerNameLeftOne = playerNameLeftOne!!,
        playerTeamIdLeftOne = playerTeamIdLeftOne,
        playerTeamNameLeftOne = playerTeamNameLeftOne,
        playerIdLeftTwo = playerIdLeftTwo,
        playerNameLeftTwo = playerNameLeftTwo,
        playerTeamIdLeftTwo = playerTeamIdLeftTwo,
        playerTeamNameLeftTwo = playerTeamNameLeftTwo,
        playerIdRightOne = playerIdRightOne,
        playerNameRightOne = playerNameRightOne,
        playerTeamIdRightOne = playerTeamIdRightOne,
        playerTeamNameRightOne = playerTeamNameRightOne,
        playerIdRightTwo = playerIdRightTwo,
        playerNameRightTwo = playerNameRightTwo,
        playerTeamIdRightTwo = playerTeamIdRightTwo,
        playerTeamNameRightTwo = playerTeamNameRightTwo,
        sets = sets,
        leftWon =
            if (sets.isEmpty())
                null
            else
                sets.count { it.leftPoints > it.rightPoints } > sets.count { it.leftPoints < it.rightPoints }
    )