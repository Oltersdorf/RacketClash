package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.CategoryDatabase
import com.olt.racketclash.database.api.CategoryFilter
import com.olt.racketclash.database.api.CategorySorting
import com.olt.racketclash.database.api.FilteredSortedList
import kotlin.math.min

internal class CategoryDatabaseImpl : CategoryDatabase {
    private val categories = mutableListOf<Category>()

    override suspend fun selectList(
        filter: CategoryFilter,
        sorting: CategorySorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Category, CategoryFilter, CategorySorting> =
        FilteredSortedList(
            totalSize = categories.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = categories.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), categories.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectLast(n: Long): List<Category> =
        categories.takeLast(n.toInt())

    override suspend fun selectSingle(id: Long): Category =
        categories.first { it.id == id }

    override suspend fun add(category: Category) {
        categories.add(category)
    }

    override suspend fun update(category: Category) {
        categories.replaceAll {
            if (it.id == category.id)
                category
            else
                it
        }
    }

    override suspend fun delete(id: Long) {
        categories.removeIf { it.id == id }
    }
}