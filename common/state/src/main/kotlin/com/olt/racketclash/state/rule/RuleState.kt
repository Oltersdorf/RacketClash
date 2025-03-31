package com.olt.racketclash.state.rule

import com.olt.racketclash.database.api.Rule

data class RuleState(
    val isLoading: Boolean = true,
    val rule: Rule = Rule()
)