package com.olt.racketclash.database.cache

internal class Cache<T>(
    private val size: Int,
    private val cache: ArrayDeque<T> = ArrayDeque()
) {
    init {
        require(size > 0) { "Size must be greater than 0" }
    }

    fun find(selector: (T) -> Boolean): Result<T> {
        val cacheResult = cache.find(selector)
        return if (cacheResult != null)
            Result.Hit(value = cacheResult)
        else
            Result.Miss
    }

    fun add(value: T) {
        if (cache.size == size)
            cache.removeLast()

        cache.addFirst(value)
    }

    fun findOrAdd(selector: (T) -> Boolean, valueFactory: () -> T): T {
        val result = find(selector)

        if (result is Result.Hit)
            return result.value
        else {
            val value = valueFactory()
            add(value)
            return value
        }
    }

    sealed class Result<out T> {
        data class Hit<T>(val value: T) : Result<T>()
        data object Miss : Result<Nothing>()
    }
}