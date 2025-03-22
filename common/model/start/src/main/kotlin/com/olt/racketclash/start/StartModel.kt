package com.olt.racketclash.start

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.rule.Filter
import com.olt.racketclash.database.rule.Sorting as RuleSorting
import com.olt.racketclash.state.ViewModelState
import com.olt.racketclash.database.player.Sorting as PlayerSorting
import com.olt.racketclash.database.tournament.Sorting as TournamentSorting

class StartModel(
    private val database: Database
) : ViewModelState<State>(initialState = State()) {

    init {
        onIO {
            updateState {
                val tournaments = database.tournaments.selectFilteredAndOrdered(
                    nameFilter = "",
                    locationFilter = "",
                    yearFilter = null,
                    sorting = TournamentSorting.NameAsc,
                    fromIndex = 0,
                    toIndex = 5
                ).second.take(5)

                val players = database.players.selectFilteredAndOrdered(
                    nameFilter = "",
                    birthYearFilter = null,
                    clubFilter = "",
                    hasMedalsFilter = null,
                    sorting = PlayerSorting.NameAsc,
                    fromIndex = 0,
                    toIndex = 5
                ).second.take(5)

                val rules = database.rules.selectFilteredAndOrdered(
                    filter = Filter(),
                    sorting = RuleSorting.NameAsc,
                    fromIndex = 0,
                    toIndex = 5
                ).second.take(5)

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