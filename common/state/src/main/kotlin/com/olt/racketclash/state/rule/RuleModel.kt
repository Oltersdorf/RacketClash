package com.olt.racketclash.state.rule

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.toFilteredAndOrderedRule
import com.olt.racketclash.database.table.FilteredAndOrderedRule
import com.olt.racketclash.state.ViewModelState

class RuleModel(
    private val database: Database,
    id: Long
) : ViewModelState<RuleState>(initialState = RuleState()) {

    val tournaments = lazy {  }

    init {
        onIO {
            updateState {
                copy(
                    rule = database.rules.selectSingle(id = id).toFilteredAndOrderedRule()
                )
            }
        }
    }

    fun updateRule(rule: FilteredAndOrderedRule) {
        onIO {
            updateState { copy(rule = rule) }
            database.rules.update(rule = rule)
        }
    }
}