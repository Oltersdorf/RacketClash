package com.olt.racketclash.database

import java.time.*
import java.time.format.DateTimeFormatter

class DateTimeConverter {

    val defaultZoneId: ZoneId = ZoneId.systemDefault()

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm z")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun toString(unixDateTimeSeconds: Long, zoneId: ZoneId = defaultZoneId): String =
        ZonedDateTime
            .ofInstant(Instant.ofEpochSecond(unixDateTimeSeconds), zoneId)
            .format(dateTimeFormatter)

    fun addToString(unixDateSeconds: Long, time: String, zoneId: ZoneId = defaultZoneId): String =
        LocalDateTime
            .of(LocalDate.ofInstant(Instant.ofEpochSecond(unixDateSeconds), zoneId), LocalTime.parse(time, timeFormatter))
            .format(dateTimeFormatter.withZone(zoneId))

    fun toLong(dateTime: String, zoneId: ZoneId = defaultZoneId): Long =
        ZonedDateTime
            .parse(dateTime, dateTimeFormatter.withZone(zoneId))
            .toEpochSecond()

    fun yearStart(year: Int, zoneId: ZoneId = defaultZoneId): Long =
        LocalDate
            .of(year, 1, 1)
            .atStartOfDay(zoneId)
            .toEpochSecond()

    fun yearEnd(year: Int, zoneId: ZoneId = defaultZoneId): Long =
        LocalDate
            .of(year + 1, 1, 1)
            .atStartOfDay(zoneId)
            .toEpochSecond() - 1

    fun toTime(unixDateTimeSeconds: Long, zoneId: ZoneId = defaultZoneId): String =
        ZonedDateTime
            .ofInstant(Instant.ofEpochSecond(unixDateTimeSeconds), zoneId)
            .format(timeFormatter)
}