package com.olt.racketclash.state.start

import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.database.api.RuleDatabase
import com.olt.racketclash.database.api.TournamentDatabase
import com.olt.racketclash.state.ViewModelState

class StartModel(
    tournamentDatabase: TournamentDatabase,
    playerDatabase: PlayerDatabase,
    ruleDatabase: RuleDatabase
) : ViewModelState<StartState>(initialState = StartState()) {

    init {
        onIO {
            val tournaments = tournamentDatabase.selectLast(n = 5)
            val players = playerDatabase.selectLast(n = 5)
            val rules = ruleDatabase.selectLast(n = 5)

            updateState {
                copy(
                    isLoading = false,
                    tournaments = tournaments,
                    players = players,
                    rules = rules
                )
            }
        }
    }
}