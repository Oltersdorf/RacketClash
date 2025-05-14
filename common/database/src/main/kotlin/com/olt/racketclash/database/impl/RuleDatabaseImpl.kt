package com.olt.racketclash.database.impl

import com.olt.racketclash.database.api.FilteredSortedList
import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.database.api.RuleDatabase
import com.olt.racketclash.database.api.RuleFilter
import com.olt.racketclash.database.api.RuleSorting
import kotlin.math.min

internal class RuleDatabaseImpl: RuleDatabase {
    private val rules = mutableListOf<Rule>()

    override suspend fun selectList(
        filter: RuleFilter,
        sorting: RuleSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Rule, RuleFilter, RuleSorting> =
        FilteredSortedList(
            totalSize = rules.size.toLong(),
            fromIndex = fromIndex,
            toIndex = toIndex,
            items = rules.toList().subList(fromIndex.toInt(), min(toIndex.toInt(), rules.size)),
            filter = filter,
            sorting = sorting
        )

    override suspend fun selectLast(n: Long): List<Rule> =
        rules.takeLast(n.toInt())

    override suspend fun selectSingle(id: Long): Rule =
        rules.first { it.id == id }

    override suspend fun add(rule: Rule) {
        rules.add(rule)
    }

    override suspend fun update(rule: Rule) {
        rules.replaceAll {
            if (it.id == rule.id)
                rule
            else
                it
        }
    }

    override suspend fun delete(id: Long) {
        rules.removeIf { it.id == id }
    }
}