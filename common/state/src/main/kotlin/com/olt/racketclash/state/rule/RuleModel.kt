package com.olt.racketclash.state.rule

import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.database.api.RuleDatabase
import com.olt.racketclash.state.ViewModelState

class RuleModel(
    private val ruleDatabase: RuleDatabase,
    id: Long
) : ViewModelState<RuleState>(initialState = RuleState()) {

    val tournaments = lazy {  }

    init {
        onIO {
            val rule = ruleDatabase.selectSingle(id = id)
            updateState { copy(rule = rule) }
        }
    }

    fun updateRule(rule: Rule) {
        onIO {
            updateState { copy(rule = rule) }
            ruleDatabase.update(rule = rule)
        }
    }
}