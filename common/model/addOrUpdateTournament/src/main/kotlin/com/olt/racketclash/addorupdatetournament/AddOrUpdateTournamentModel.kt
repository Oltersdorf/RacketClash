package com.olt.racketclash.addorupdatetournament

import com.olt.racketclash.database.api.TournamentDatabase
import com.olt.racketclash.state.ViewModelState
import java.time.Instant

class AddOrUpdateTournamentModel(
    private val database: TournamentDatabase,
    private val tournamentId: Long?
) : ViewModelState<State>(initialState = State()) {

    init {
        onDefault {
            val times = mutableListOf<String>()

            for (hour in 0..23)
                for (minute in 0..45 step 15)
                    times += "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

            updateState { copy(suggestedTimes = times) }
        }

        onIO {
            if (tournamentId == null)
                updateState { copy(isLoading = false) }
            else {
                val loadedTournament = database.selectSingle(id = tournamentId)
                val locations = database.locations(filter = loadedTournament.location)

                updateState {
                    copy(
                        isSavable = true,
                        isLoading = false,
                        tournament = loadedTournament,
                        startDateMillis = loadedTournament.start.epochSecond * 1000,
                        endDateMillis = loadedTournament.end.epochSecond * 1000,
                        timeStart = "",
                        timeEnd = "",
                        suggestedLocations = locations
                    )
                }
            }
        }
    }

    fun updateName(newName: String) =
        updateState {
            copy(
                tournament = tournament.copy(name = newName),
                isSavable = newName.isNotBlank() && startDateMillis != null && endDateMillis != null
            )
        }

    fun updateLocation(newLocation: String) {
        updateState { copy(tournament = tournament.copy(location = newLocation)) }

        onIO {
            val suggestedLocations = database.locations(filter = newLocation)
            updateState { copy(suggestedLocations = suggestedLocations) }
        }
    }

    fun updateDateRange(start: Long?, end: Long?) =
        updateState {
            copy(
                startDateMillis = start,
                endDateMillis = end,
                isSavable = tournament.name.isNotBlank() && start != null && end != null
            )
        }

    fun updateTimeStart(newTimeStart: String) =
        updateState { copy(timeStart = newTimeStart) }

    fun updateTimeEnd(newTimeEnd: String) =
        updateState { copy(timeEnd = newTimeEnd) }

    fun updateCourts(newCourts: Int) =
        updateState { copy(tournament = tournament.copy(numberOfCourts = newCourts)) }

    fun save(onComplete: () -> Unit = {}) =
        onIO {
            updateState { copy(isLoading = true) }

            val state = state.value

            val newTournament = state.tournament.copy(
                start = Instant.ofEpochMilli(state.startDateMillis ?: 0),
                end = Instant.ofEpochMilli(state.endDateMillis ?: 0)
            )

            if (tournamentId == null)
                database.add(tournament = newTournament)
            else
                database.update(tournament = newTournament)

            updateState { copy(isLoading = false) }

            onMain { onComplete() }
        }
}