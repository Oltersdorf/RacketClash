package com.olt.racketclash.database.rule

import com.olt.racketclash.database.RacketClashDatabase

class RuleDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrdered(
        nameFilter: String,
        sorting: Sorting,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<DeletableRule>> =
        database.ruleQueries.selectFilteredAndOrderedSize(name = nameFilter).executeAsOne() to
        database.ruleQueries.selectFilteredAndOrdered(
            name = nameFilter,
            sorting = sorting.name,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList().map { it.toDeletableRule() }

    fun selectSingle(id: Long): DeletableRule =
        database.ruleQueries.selectSingle(id = id).executeAsOne().toDeletableRule()

    fun add(rule: DeletableRule) =
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

    fun update(rule: DeletableRule) =
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