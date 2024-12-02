package com.olt.racketclash.database.schedule

import com.olt.racketclash.database.DateTimeConverter

data class Schedule(
    val id: Long,
    val ruleId: Long,
    val ruleName: String,
    val maxSets: Int,
    val scheduledFor: String,
    val active: Boolean,
    val categoryId: Long,
    val categoryName: String,
    val categoryOrderNumber: Int,
    val playerIdLeftOne: Long,
    val playerNameLeftOne: String,
    val playerIdLeftTwo: Long?,
    val playerNameLeftTwo: String?,
    val playerIdRightOne: Long,
    val playerNameRightOne: String,
    val playerIdRightTwo: Long?,
    val playerNameRightTwo: String?
)

fun SelectFilteredAndOrdered.toSchedule(dateTimeConverter: DateTimeConverter) =
    Schedule(
        id = id,
        ruleId = ruleId,
        ruleName = ruleName ?: "",
        maxSets = maxSets ?: 1,
        scheduledFor = dateTimeConverter.toString(unixDateTimeSeconds = unixTimeScheduled, zoneId = zoneId),
        active = active,
        categoryId = categoryId,
        categoryName = categoryName ?: "",
        categoryOrderNumber = categoryOrderNumber,
        playerIdLeftOne = playerIdLeftOne,
        playerNameLeftOne = playerNameLeftOne ?: "",
        playerIdLeftTwo = playerIdLeftTwo,
        playerNameLeftTwo = playerNameLeftTwo,
        playerIdRightOne = playerIdRightOne,
        playerNameRightOne = playerNameRightOne ?: "",
        playerIdRightTwo = playerIdRightTwo,
        playerNameRightTwo = playerNameRightTwo
    )