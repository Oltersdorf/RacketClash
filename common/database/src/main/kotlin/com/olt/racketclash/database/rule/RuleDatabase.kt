package com.olt.racketclash.database.rule

import com.olt.racketclash.database.RacketClashDatabase
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.database.table.Rule

class RuleDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        nameFilter: String,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<FilteredAndOrderedRule>> =
        database.ruleQueries.selectFilteredAndOrderedSize(name = nameFilter).executeAsOne() to
        database.ruleQueries.filteredAndOrderedRule(
            name = nameFilter,
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

    fun update(rule: Rule) =
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