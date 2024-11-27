package com.olt.racketclash.ui.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.olt.racketclash.addorupdatetournament.AddOrUpdateTournamentModel
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.layout.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddOrUpdateTournament(
    database: Database,
    tournamentId: Long?,
    tournamentName: String?,
    navigateBack: () -> Unit
) {
    val model = remember { AddOrUpdateTournamentModel(database = database, tournamentId = tournamentId) }
    val state by model.state.collectAsState()
    val dateRange = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedStartDateMillis = state.startDateMillis,
        initialSelectedEndDateMillis = state.endDateMillis
    )
    model.updateDateRange(start = dateRange.selectedStartDateMillis, end = dateRange.selectedEndDateMillis)

    Form(
        title = tournamentName ?: "New tournament",
        isLoading = state.isLoading,
        isSavable = state.isSavable,
        onSave = { model.save(onComplete = navigateBack) }
    ) {
        FormTextField(
            value = state.tournament.name,
            label = "Name",
            isError = !state.isSavable,
            onValueChange = model::updateName
        )

        FormRow {
            FormDropDownTextField(
                text = state.tournament.location,
                label = "Location",
                onTextChange = model::updateLocation,
                dropDownItems = state.suggestedLocations,
                dropDownItemText = { Text(it) },
                onItemClicked = model::updateLocation
            )

            FormNumberSelector(
                value = state.tournament.numberOfCourts,
                label = "Courts",
                range = 1..Int.MAX_VALUE,
                onUp = model::updateCourts,
                onDown = model::updateCourts
            )
        }

        FormDateRangePicker(state = dateRange)

        FormRow {
            FormDropDownTextField(
                text = state.timeStart,
                label = "Start time",
                readOnly = true,
                dropDownItems = state.suggestedTimes,
                dropDownItemText = { Text(it) },
                onItemClicked = model::updateTimeStart
            )

            FormDropDownTextField(
                text = state.timeEnd,
                label = "End time",
                readOnly = true,
                dropDownItems = state.suggestedTimes,
                dropDownItemText = { Text(it) },
                onItemClicked = model::updateTimeEnd
            )
        }
    }
}