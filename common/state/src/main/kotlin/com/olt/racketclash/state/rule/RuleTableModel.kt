package com.olt.racketclash.state.rule

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.Filter
import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.state.list.ListModel

class RuleTableModel(
    private val database: Database
) : ListModel<FilteredAndOrderedRule, Sorting, Filter>(
    initialSorting = Sorting.NameAsc,
    initialFilter = Filter()
) {
    override fun databaseDelete(item: FilteredAndOrderedRule) =
        database.rules.delete(id = item.id)

    override fun databaseSelect(
        sorting: Sorting,
        filter: Filter?,
        fromIndex: Int,
        toIndex: Int
    ): Pair<Long, List<FilteredAndOrderedRule>> =
        database.rules.selectFilteredAndOrdered(
            filter = filter ?: Filter(),
            sorting = sorting,
            fromIndex = fromIndex,
            toIndex = toIndex
        )

    override fun databaseAdd(item: FilteredAndOrderedRule) =
        database.rules.add(rule = item)
}