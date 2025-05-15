package com.olt.racketclash.state.club

import com.olt.racketclash.database.api.Club
import com.olt.racketclash.database.api.ClubDatabase
import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.database.api.TournamentDatabase
import com.olt.racketclash.state.ViewModelState

class ClubModel(
    private val clubDatabase: ClubDatabase,
    playerDatabase: PlayerDatabase,
    tournamentDatabase: TournamentDatabase,
    clubId: Long
) : ViewModelState<ClubState>(initialState = ClubState()) {

    init {
        onIO {
            val club = clubDatabase.selectSingle(id = clubId)
            val players = playerDatabase.selectLast(n = 5)
            val tournaments = tournamentDatabase.selectLast(n = 5)

            updateState {
                copy(
                    isLoading = false,
                    club = club,
                    players = players,
                    tournaments = tournaments
                )
            }
        }
    }

    fun updateClub(club: Club) {
        onIO {
            updateState { copy(club = club) }
            clubDatabase.update(club = club)
        }
    }
}