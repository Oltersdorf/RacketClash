package com.olt.racketclash.database.rule

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.database.table.Rule

class RuleDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        filter: Filter,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<FilteredAndOrderedRule>> =
        database.ruleQueries.selectFilteredAndOrderedSize(name = filter.name).executeAsOne() to
        database.ruleQueries.filteredAndOrderedRule(
            name = filter.name,
            sorting = sorting.name,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList()

    fun selectSingle(id: Long): Rule =
        database.ruleQueries.rule(id = id).executeAsOne()

    fun add(rule: Rule) =
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

    fun add(rule: FilteredAndOrderedRule) =
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

    fun update(rule: FilteredAndOrderedRule) =
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

    fun delete(id: Long) =
        database.ruleQueries.delete(id = id)
}