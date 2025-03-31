package com.olt.racketclash.state.tournament

import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.ViewModelState

class TournamentModel(
    private val tournamentDatabase: TournamentDatabase,
    playerDatabase: PlayerDatabase,
    teamDatabase: TeamDatabase,
    categoryDatabase: CategoryDatabase,
    gameDatabase: GameDatabase,
    scheduleDatabase: ScheduleDatabase,
    tournamentId: Long
) : ViewModelState<TournamentState>(initialState = TournamentState()) {

    init {
        onIO {
            val tournament = tournamentDatabase.selectSingle(id = tournamentId)
            val players = playerDatabase.selectLast(n = 5)
            val teams = teamDatabase.selectLast(n = 5)
            val categories = categoryDatabase.selectLast(n = 5)
            val games = gameDatabase.selectLast(n = 5)
            val scheduledGames = scheduleDatabase.selectFirst(n = 5)
            val locationSuggestions = tournamentDatabase.locations(filter = tournament.location)

            updateState {
                copy(
                    isLoading = false,
                    tournament = tournament,
                    players = players,
                    teams = teams,
                    categories = categories,
                    games = games,
                    scheduledGames = scheduledGames,
                    locationSuggestions = locationSuggestions
                )
            }
        }
    }

    fun updateTournament(tournament: Tournament) {
        onIO {
            updateState { copy(tournament = tournament) }
            tournamentDatabase.update(tournament = tournament)
        }
    }

    fun locationSuggestions(filter: String) {
        onIO {
            val locationSuggestions = tournamentDatabase.locations(filter = filter)
            updateState { copy(locationSuggestions = locationSuggestions) }
        }
    }
}