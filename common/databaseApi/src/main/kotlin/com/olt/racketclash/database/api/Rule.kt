package com.olt.racketclash.database.api

data class RuleFilter(
    val name: String = ""
)

sealed interface RuleSorting {
    data object NameAsc : RuleSorting
    data object NameDesc : RuleSorting
}

data class Rule(
    val id: Long = -1,
    val name: String = "",
    val maxSets: Int = 3,
    val winSets: Int = 2,
    val maxPoints: Int = 30,
    val winPoints: Int = 21,
    val pointsDifference: Int = 2,
    val gamePointsForWin: Int = 2,
    val gamePointsForLose: Int = 0,
    val gamePointsForDraw: Int = 1,
    val gamePointsForRest: Int = 2,
    val setPointsForRest: Int = 0,
    val pointPointsForRest: Int = 0,
    val used: Long = 0L
): Validateable {

    override fun validate(): Boolean {
        return name.isNotBlank()
    }
}

interface RuleDatabase {
    suspend fun selectList(
        filter: RuleFilter,
        sorting: RuleSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Rule, RuleFilter, RuleSorting>

    suspend fun selectLast(n: Long): List<Rule>

    suspend fun selectSingle(id: Long): Rule

    suspend fun add(rule: Rule)

    suspend fun update(rule: Rule)

    suspend fun delete(id: Long)
}