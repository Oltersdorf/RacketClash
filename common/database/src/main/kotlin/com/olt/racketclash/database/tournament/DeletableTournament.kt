package com.olt.racketclash.database.tournament

import com.olt.racketclash.database.DateTimeConverter

data class DeletableTournament(
    val id: Long,
    val name: String,
    val numberOfCourts: Int,
    val location: String,
    val startDateTime: String,
    val endDateTime: String,
    val playersCount: Int,
    val categoriesCount: Int,
    val deletable: Boolean
)

internal fun SelectFilteredAndOrdered.toDeletableTournament(dateTimeConverter: DateTimeConverter) =
    DeletableTournament(
        id = id,
        name = name,
        numberOfCourts = numberOfCourts,
        location = location,
        startDateTime = dateTimeConverter.toString(unixDateTimeSeconds = unixStartDateTime, zoneId = zoneId),
        endDateTime = dateTimeConverter.toString(unixDateTimeSeconds = unixEndDateTime, zoneId = zoneId),
        playersCount = playersCount.toInt(),
        categoriesCount = categoriesCount.toInt(),
        deletable = deletable == 1L
    )

internal fun SelectSingle.toDeletableTournament(dateTimeConverter: DateTimeConverter) =
    DeletableTournament(
        id = id,
        name = name,
        numberOfCourts = numberOfCourts,
        location = location,
        startDateTime = dateTimeConverter.toString(unixDateTimeSeconds = unixStartDateTime, zoneId = zoneId),
        endDateTime = dateTimeConverter.toString(unixDateTimeSeconds = unixEndDateTime, zoneId = zoneId),
        playersCount = playersCount.toInt(),
        categoriesCount = categoriesCount.toInt(),
        deletable = false
    )