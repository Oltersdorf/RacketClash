package com.olt.racketclash.data.database

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

open class TimeStamp {
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    fun currentTime(): String = LocalDateTime.now().format(dateTimeFormatter)
}