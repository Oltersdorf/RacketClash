package com.olt.racketclash.ui.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var isLoading by remember { mutableStateOf(false) }
    var isSavable by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val locations = remember { listOf("Location 1", "Location 2") }
    var courts by remember { mutableStateOf(1) }
    val dateRange = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Input)
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    val timeDropDownItems = remember {
        (0..2400 step 15).map {
            it
                .toString()
                .padStart(4, '0')
                .chunked(2)
                .joinToString(separator = ":")
        }
    }

    Form(
        title = tournamentName ?: "New tournament",
        isLoading = isLoading,
        isSavable = isSavable && dateRange.selectedStartDateMillis != null && dateRange.selectedEndDateMillis != null,
        onSave = {
            navigateBack()
        }
    ) {
        FormTextField(value = name, label = "Name", isError = !isSavable) {
            name = it
            isSavable = name.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank()
        }

        FormRow {
            FormDropDownTextField(
                text = location,
                label = "Location",
                onTextChange = { location = it },
                dropDownItems = locations,
                dropDownItemText = { Text(it) },
                onItemClicked = { location = it }
            )

            FormNumberSelector(
                value = courts,
                label = "Courts",
                range = 1..Int.MAX_VALUE,
                onUp = { courts = it },
                onDown = { courts = it }
            )
        }

        FormDateRangePicker(state = dateRange)

        FormRow {
            FormDropDownTextField(
                text = startTime,
                label = "Start time",
                readOnly = true,
                dropDownItems = timeDropDownItems,
                dropDownItemText = { Text(it) },
                onItemClicked = {
                    startTime = it
                    isSavable = name.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank()
                }
            )

            FormDropDownTextField(
                text = endTime,
                label = "End time",
                readOnly = true,
                dropDownItems = timeDropDownItems,
                dropDownItemText = { Text(it) },
                onItemClicked = {
                    endTime = it
                    isSavable = name.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank()
                }
            )
        }
    }
}