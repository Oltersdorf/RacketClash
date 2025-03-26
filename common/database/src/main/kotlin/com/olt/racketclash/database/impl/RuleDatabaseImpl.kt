package com.olt.racketclash.database.impl

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.api.*
import com.olt.racketclash.database.toName
import com.olt.racketclash.database.toRule

internal class RuleDatabaseImpl(
    private val database: RacketClashDatabase
) : RuleDatabase {

    override suspend fun selectList(
        filter: RuleFilter,
        sorting: RuleSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Rule, RuleFilter, RuleSorting> =
        database.transactionWithResult {
            FilteredSortedList(
                totalSize = database
                    .ruleQueries
                    .selectFilteredAndOrderedSize(name = filter.name)
                    .executeAsOne(),
                fromIndex = fromIndex,
                toIndex = toIndex,
                items = database
                    .ruleQueries
                    .filteredAndOrderedRule(
                        name = filter.name,
                        sorting = sorting.toName(),
                        offset = fromIndex,
                        limit = toIndex
                    ).executeAsList()
                    .map { it.toRule() },
                filter = filter,
                sorting = sorting
            )
        }

    override suspend fun selectLast(n: Long): List<Rule> =
        database
            .ruleQueries
            .selectLast(n = n)
            .executeAsList()
            .map { it.toRule() }

    override suspend fun selectSingle(id: Long): Rule =
        database
            .ruleQueries
            .rule(id = id)
            .executeAsOne()
            .toRule()

    override suspend fun add(rule: Rule) =
        database.ruleQueries.add(
            name = rule.name,
            maxSets = rule.maxSets,
            winSets = rule.winSets,
            maxPoints = rule.maxPoints,
            winPoints = rule.winPoints,
            pointsDifference = rule.pointsDifference,
            gamePointsForWin = rule.gamePointsForWin,
            gamePointsForLose = rule.gamePointsForLose,
            gamePointsForDraw = rule.gamePointsForDraw,
            gamePointsForRest = rule.gamePointsForRest,
            setPointsForRest = rule.setPointsForRest,
            pointPointsForRest = rule.pointPointsForRest
        )

    override suspend fun update(rule: Rule) =
        database.ruleQueries.update(
            id = rule.id,
            name = rule.name,
            maxSets = rule.maxSets,
            winSets = rule.winSets,
            maxPoints = rule.maxPoints,
            winPoints = rule.winPoints,
            pointsDifference = rule.pointsDifference,
            gamePointsForWin = rule.gamePointsForWin,
            gamePointsForLose = rule.gamePointsForLose,
            gamePointsForDraw = rule.gamePointsForDraw,
            gamePointsForRest = rule.gamePointsForRest,
            setPointsForRest = rule.setPointsForRest,
            pointPointsForRest = rule.pointPointsForRest
        )

    override suspend fun delete(id: Long) =
        database.ruleQueries.delete(id = id)
}