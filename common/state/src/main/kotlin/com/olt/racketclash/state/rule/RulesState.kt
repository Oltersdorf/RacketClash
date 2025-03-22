package com.olt.racketclash.state.rule

import com.olt.racketclash.database.rule.Filter

data class RulesState(
    val filter: Filter = Filter()
)