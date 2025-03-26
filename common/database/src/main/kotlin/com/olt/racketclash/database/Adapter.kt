package com.olt.racketclash.database

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.olt.racketclash.database.api.CategoryType
import com.olt.racketclash.database.table.*

private val CategoryTypeColumnAdapter = object : ColumnAdapter<CategoryType, Long> {
    override fun decode(databaseValue: Long): CategoryType =
        when (databaseValue) {
            0L -> CategoryType.Custom
            1L -> CategoryType.Table
            2L -> CategoryType.Tree
            else -> CategoryType.Custom
        }

    override fun encode(value: CategoryType): Long =
        when (value) {
            CategoryType.Custom -> 0L
            CategoryType.Table -> 1L
            CategoryType.Tree -> 2L
        }
}

internal fun categoryAdapter() =
    Category.Adapter(typeAdapter = CategoryTypeColumnAdapter)

internal fun gameAdapter() =
    Game.Adapter(categoryOrderNumberAdapter = IntColumnAdapter)

internal fun playerAdapter() =
    PlayerTable.Adapter(birthYearAdapter = IntColumnAdapter)

internal fun ruleAdapter() =
    RuleTable.Adapter(
        maxSetsAdapter = IntColumnAdapter,
        winSetsAdapter = IntColumnAdapter,
        maxPointsAdapter = IntColumnAdapter,
        winPointsAdapter = IntColumnAdapter,
        pointsDifferenceAdapter = IntColumnAdapter,
        gamePointsForWinAdapter = IntColumnAdapter,
        gamePointsForLoseAdapter = IntColumnAdapter,
        gamePointsForDrawAdapter = IntColumnAdapter,
        gamePointsForRestAdapter = IntColumnAdapter,
        setPointsForRestAdapter = IntColumnAdapter,
        pointPointsForRestAdapter = IntColumnAdapter
    )

internal fun scheduleAdapter() =
    Schedule.Adapter(categoryOrderNumberAdapter = IntColumnAdapter)

internal fun teamAdapter() =
    TeamTable.Adapter(rankAdapter = IntColumnAdapter)

internal fun tournamentAdapter() =
    Tournament.Adapter(numberOfCourtsAdapter = IntColumnAdapter)

internal fun gameSetAdapter() =
    GameSet.Adapter(
        orderNumberAdapter = IntColumnAdapter,
        leftPointsAdapter = IntColumnAdapter,
        rightPointsAdapter = IntColumnAdapter
    )