package com.olt.racketclash.state.team

import com.olt.racketclash.database.api.PlayerDatabase
import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamDatabase
import com.olt.racketclash.state.ViewModelState

class TeamModel(
    private val teamDatabase: TeamDatabase,
    playerDatabase: PlayerDatabase,
    teamId: Long
) : ViewModelState<TeamState>(initialState = TeamState()) {

    init {
        onIO {
            val team = teamDatabase.selectSingle(id = teamId)
            val players = playerDatabase.selectLast(n = 5)

            updateState {
                copy(
                    isLoading = false,
                    team = team,
                    players = players
                )
            }
        }
    }

    fun updateTeam(team: Team) {
        onIO {
            updateState { copy(team = team) }
            teamDatabase.update(team = team)
        }
    }
}