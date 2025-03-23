package com.olt.racketclash.state.rule

import com.olt.racketclash.database.rule.emptyRule
import com.olt.racketclash.database.table.FilteredAndOrderedRule

data class RuleState(
    val rule: FilteredAndOrderedRule = emptyRule()
)