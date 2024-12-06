package com.olt.rackeclash.addorupdaterule

import com.olt.racketclash.database.rule.emptyRule
import com.olt.racketclash.database.table.Rule

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val rule: Rule = emptyRule()
)