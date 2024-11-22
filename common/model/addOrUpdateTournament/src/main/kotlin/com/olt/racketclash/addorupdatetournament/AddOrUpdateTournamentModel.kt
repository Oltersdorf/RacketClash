package com.olt.racketclash.addorupdatetournament

import com.olt.racketclash.database.Database
import com.olt.racketclash.state.ViewModelState

class AddOrUpdateTournamentModel(
    private val database: Database,
    private val tournamentId: Long?
) : ViewModelState<State>(initialState = State()) {

    init {
        onDefault {
            updateState {
                copy(suggestedTimes = (0..2400 step 15)
                    .map {
                        Time(hour = it / 100, minute = it % 100)
                    }
                )
            }
        }
    }

    fun updateName(newName: String) =
        updateState {
            copy(
                name = newName,
                isSavable = newName.isNotBlank() && dateRangeStart != null && dateRangeEnd != null
            )
        }

    fun updateLocation(newLocation: String) {
        updateState { copy(location = newLocation) }

        onIO {
            //update suggestedLocations
        }
    }

    fun updateDateRange(start: Long?, end: Long?) =
        updateState {
            copy(
                dateRangeStart = start,
                dateRangeEnd = end,
                isSavable = name.isNotBlank() && start != null && end != null
            )
        }

    fun updateTimeStart(newTimeStart: Time) =
        updateState { copy(timeStart = newTimeStart) }

    fun updateTimeEnd(newTimeEnd: Time) =
        updateState { copy(timeEnd = newTimeEnd) }

    fun updateCourts(newCourts: Int) =
        updateState { copy(courts = newCourts) }

    fun save() =
        onIO {
            //write to database
        }
}