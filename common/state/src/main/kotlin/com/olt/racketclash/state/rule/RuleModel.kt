package com.olt.racketclash.state.rule

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.ViewModelState

class RuleModel(
    private val ruleDatabase: RuleDatabase,
    tournamentDatabase: TournamentDatabase,
    categoryDatabase: CategoryDatabase,
    gameDatabase: GameDatabase,
    ruleId: Long
) : ViewModelState<RuleState>(initialState = RuleState()) {

    init {
        onIO {
            val rule = ruleDatabase.selectSingle(id = ruleId)
            val tournaments = tournamentDatabase.selectLast(n = 5)
            val categories = categoryDatabase.selectLast(n = 5)
            val games = gameDatabase.selectLast(n = 5)

            updateState {
                copy(
                    isLoading = false,
                    rule = rule,
                    tournaments = tournaments,
                    categories = categories,
                    games = games
                )
            }
        }
    }

    fun updateRule(rule: Rule) {
        onIO {
            updateState { copy(rule = rule) }
            ruleDatabase.update(rule = rule)
        }
    }
}