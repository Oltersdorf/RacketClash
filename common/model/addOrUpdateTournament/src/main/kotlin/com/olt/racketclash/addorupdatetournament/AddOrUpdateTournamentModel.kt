package com.olt.racketclash.addorupdatetournament

import com.olt.racketclash.database.Database
import com.olt.racketclash.database.DateTimeConverter
import com.olt.racketclash.state.ViewModelState

class AddOrUpdateTournamentModel(
    private val database: Database,
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
                val loadedTournament = database.tournaments.selectSingle(id = tournamentId)
                val dateTimeConverter = DateTimeConverter()

                val startDateSeconds = dateTimeConverter.toLong(dateTime = loadedTournament.startDateTime)
                val endDateSeconds = dateTimeConverter.toLong(dateTime = loadedTournament.startDateTime)

                updateState {
                    copy(
                        isSavable = true,
                        isLoading = false,
                        tournament = loadedTournament,
                        startDateMillis = startDateSeconds * 1000,
                        endDateMillis = endDateSeconds * 1000,
                        timeStart = dateTimeConverter.toTime(unixDateTimeSeconds = startDateSeconds),
                        timeEnd = dateTimeConverter.toTime(unixDateTimeSeconds = endDateSeconds),
                        suggestedLocations = database.tournaments.locations(filter = loadedTournament.location)
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
            updateState {
                copy(suggestedLocations = database.tournaments.locations(filter = newLocation))
            }
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
            val dateTimeConverter = DateTimeConverter()

            val newTournament = state.tournament.copy(
                startDateTime = dateTimeConverter.addToString(
                    unixDateSeconds = state.startDateMillis!! / 1000,
                    time = state.timeStart
                ),
                endDateTime = dateTimeConverter.addToString(
                    unixDateSeconds = state.endDateMillis!! / 1000,
                    time = state.timeEnd
                )
            )

            if (tournamentId == null)
                database.tournaments.add(tournament = newTournament)
            else
                database.tournaments.update(tournament = newTournament)

            updateState { copy(isLoading = false) }

            onMain { onComplete() }
        }
}