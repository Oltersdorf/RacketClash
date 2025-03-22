package com.olt.racketclash.state.rule

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.Filter
import com.olt.racketclash.database.rule.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.state.list.ListModel

class RuleListModel(
    private val database: Database,
    filter: () -> Filter
): ListModel<FilteredAndOrderedRule, Sorting, Filter>(
    initialSorting = Sorting.NameAsc,
    filter = filter
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
}