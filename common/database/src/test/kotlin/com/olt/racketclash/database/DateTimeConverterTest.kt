package com.olt.racketclash.database

import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeConverterTest {
    @Test
    fun testToString() {
        val dateTime = DateTimeConverter()
        val testTimeLong = 0L
        val expected = "1970/01/01 01:00 MEZ"
        val actual = dateTime.toString(unixDateTimeSeconds = testTimeLong, zoneId = ZoneId.of("Europe/Berlin"))
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun testAddToString() {
        val dateTime = DateTimeConverter()
        val testDateLong = 3600L // "1970/01/01 02:00 MEZ"
        val testTime = "03:00"
        val expected = "1970/01/01 03:00 MEZ"
        val actual = dateTime.addToString(unixDateSeconds = testDateLong, time = testTime, zoneId = ZoneId.of("Europe/Berlin"))
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun testToLong() {
        val dateTime = DateTimeConverter()
        val testString = "1970/01/01 01:00 MEZ"
        val expected = 0L
        val actual = dateTime.toLong(dateTime = testString, zoneId = ZoneId.of("Europe/Berlin"))
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun testYearStart() {
        val dateTime = DateTimeConverter()
        val testYear = 1970
        val expected = -3600L
        val actual = dateTime.yearStart(year = testYear, zoneId = ZoneId.of("Europe/Berlin"))
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun testYearEnd() {
        val dateTime = DateTimeConverter()
        val testYear = 1970
        val expected = 31532400L - 1 //year 1971 - 1 second
        val actual = dateTime.yearEnd(year = testYear, zoneId = ZoneId.of("Europe/Berlin"))
        assertEquals(expected = expected, actual = actual)
    }
}