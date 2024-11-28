package com.olt.racketclash.database.category

import app.cash.sqldelight.ColumnAdapter
import com.olt.racketclash.database.Category

fun categoryAdapter() =
    Category.Adapter(typeAdapter = CategoryTypeColumnAdapter)

private val CategoryTypeColumnAdapter = object : ColumnAdapter<CategoryType, Long> {
    override fun decode(databaseValue: Long): CategoryType =
        when (databaseValue) {
            0L -> CategoryType.Custom
            1L -> CategoryType.List
            2L -> CategoryType.Tree
            else -> CategoryType.Custom
        }

    override fun encode(value: CategoryType): Long =
        when (value) {
            CategoryType.Custom -> 0L
            CategoryType.List -> 1L
            CategoryType.Tree -> 2L
        }
}