package com.olt.racketclash.database.rule

import com.olt.racketclash.database.RacketClashDatabase

class RuleDatabase(private val database: RacketClashDatabase) {

    fun selectFilteredAndOrderedByNameAsc(
        nameFilter: String,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<DeletableRule>> =
        database.ruleQueries.sizeOfFilteredByNameAsc(name = nameFilter).executeAsOne() to
        database.ruleQueries.selectFilteredAndOrderedByNameAsc(
            name = nameFilter,
            offset = fromIndex.toLong(),
            limit = toIndex.toLong()
        ).executeAsList().map { it.toDeletableRule() }

    fun selectFilteredAndOrderedByNameDesc(
        nameFilter: String,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<DeletableRule>> =
        database.ruleQueries.sizeOfFilteredByNameDesc(name = nameFilter).executeAsOne() to
        database.ruleQueries.selectFilteredAndOrderedByNameDesc(
            name = nameFilter,
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