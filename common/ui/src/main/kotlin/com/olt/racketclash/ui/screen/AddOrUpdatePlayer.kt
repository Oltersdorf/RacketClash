package com.olt.racketclash.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.layout.Form
import com.olt.racketclash.ui.layout.FormDropDownTextField
import com.olt.racketclash.ui.layout.FormTextField

@Composable
internal fun AddOrUpdatePlayer(
    database: Database,
    playerId: Long?,
    playerName: String?,
    navigateBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var isSavable by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var birthYear by remember { mutableStateOf(1900) }
    var club by remember { mutableStateOf("") }
    val clubs by remember { mutableStateOf(listOf("Club 1", "Club 2")) }

    Form(
        title = playerName ?: "New player",
        isLoading = isLoading,
        isSavable = isSavable,
        onSave = {
            navigateBack()
        }
    ) {
        FormTextField(value = name, label = "Name", isError = !isSavable) {
            name = it
            isSavable = name.isNotBlank()
        }

        FormDropDownTextField(
            text = birthYear.toString(),
            label = "Birth year",
            readOnly = true,
            dropDownItems = (1900..2050).toList(),
            dropDownItemText = { Text(it.toString()) },
            onItemClicked = { birthYear = it }
        )

        FormDropDownTextField(
            text = club,
            label = "Club",
            onTextChange = { club = it },
            dropDownItems = clubs,
            dropDownItemText = { Text(it) },
            onItemClicked = { club = it }
        )
    }
}