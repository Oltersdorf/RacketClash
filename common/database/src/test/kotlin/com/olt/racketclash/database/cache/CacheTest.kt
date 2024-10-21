package com.olt.racketclash.database.cache

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.*

class CacheTest {

    @RepeatedTest(100)
    fun `Cache size is less than 0`() {
        val size = Int.MIN_VALUE..-1
        assertFailsWith<IllegalArgumentException>("A Cache with size $size should throw an IllegalArgumentException") { Cache<Int>(size = size.random()) }
    }

    @Test
    fun `Cache size is 0`() {
        val size = 0
        assertFailsWith<IllegalArgumentException>("A Cache with size $size should throw an IllegalArgumentException") { Cache<Int>(size = size) }
    }

    @RepeatedTest(100)
    fun `Cache size is greater than 0`() {
        val size = 1..Int.MAX_VALUE
        assertDoesNotThrow("Initializing a Cache with positive size should not throw an exception") { Cache<Int>(size = size.random()) }
    }

    @Test
    fun `find returns Cache Hit`() {
        val listValue = 1
        val cacheList = mutableListOf(listValue)
        val cache = Cache(size = 1, cache = cacheList)
        val result = cache.find { it == listValue }
        assertIs<Cache.Result.Hit<Int>>(value = result, message = "Cache Result is not a Hit")
        assertEquals(expected = listValue, actual = result.value, message = "Expected a Cache Hit of $listValue, but was ${result.value}")
    }

    @Test
    fun `find returns Cache Miss`() {
        val listValue = 1
        val cacheList = mutableListOf<Int>()
        val cache = Cache(size = 1, cache = cacheList)
        val result = cache.find { it == listValue }
        assertIs<Cache.Result.Miss>(value = result, message = "Cache Result is not a Miss")
    }

    @Test
    fun `add value to Cache`() {
        val valueToAdd = 2
        val cacheList = mutableListOf(1)
        val cache = Cache(size = 2, cache = cacheList)
        cache.add(valueToAdd)
        val expected = mutableListOf(1, valueToAdd)
        assertContentEquals(expected = expected, actual = cacheList, message = "Cache did not contain the expected values after add")
    }

    @Test
    fun `add value to Cache does not exceed initial size`() {
        val valueToAdd = 2
        val cacheList = mutableListOf(1)
        val cache = Cache(size = 1, cache = cacheList)
        cache.add(valueToAdd)
        val expected = mutableListOf(valueToAdd)
        assertContentEquals(expected = expected, actual = cacheList, message = "Cache did not contain the expected values after add")
    }

    @Test
    fun `findOrAdd returns value on Hit`() {
        val valueToFind = 1
        val cacheList = mutableListOf(valueToFind)
        val cache = Cache(size = 1, cache = cacheList)
        val result = cache.findOrAdd(selector = { it == valueToFind }) { 2 }
        assertEquals(expected = valueToFind, actual = result, message = "findOrAdd did return $result, expected was $valueToFind")
        val expectedList = mutableListOf(valueToFind)
        assertContentEquals(expected = expectedList, actual = cacheList, message = "Cache did not contain the expected values after findOrAdd")
    }

    @Test
    fun `findOrAdd adds value on Miss`() {
        val valueToFind = 1
        val cacheList = mutableListOf(2)
        val cache = Cache(size = 2, cache = cacheList)
        val result = cache.findOrAdd(selector = { it == valueToFind }) { valueToFind }
        assertEquals(expected = valueToFind, actual = result, message = "findOrAdd did return $result, expected was $valueToFind")
        val expectedList = mutableListOf(2, valueToFind)
        assertContentEquals(expected = expectedList, actual = cacheList, message = "Cache did not contain the expected values after findOrAdd")
    }
}