package com.olt.racketclash.state.rule

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.list.ListModel

class RuleTableModel(
    private val database: RuleDatabase
) : ListModel<Rule, RuleFilter, RuleSorting>(
    initialFilter = RuleFilter(),
    initialSorting = RuleSorting.NameAsc
) {
    override suspend fun databaseDelete(item: Rule) =
        database.delete(id = item.id)

    override suspend fun databaseSelect(
        filter: RuleFilter,
        sorting: RuleSorting,
        fromIndex: Long,
        toIndex: Long
    ): FilteredSortedList<Rule, RuleFilter, RuleSorting> =
        database.selectList(
            filter = filter,
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override suspend fun databaseAdd(item: Rule) =
        database.add(rule = item)
}