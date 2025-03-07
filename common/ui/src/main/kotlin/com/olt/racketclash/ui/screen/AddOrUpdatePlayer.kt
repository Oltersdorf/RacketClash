package com.olt.racketclash.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.olt.racketclash.addorupdateplayer.AddOrUpdatePlayerModel
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
    val model = remember { AddOrUpdatePlayerModel(database = database, playerId = playerId) }
    val state by model.state.collectAsState()

    Form(
        title = playerName ?: "New player",
        isLoading = state.isLoading,
        isSavable = state.isSavable,
        onSave = { model.save(onComplete = navigateBack) }
    ) {
        FormTextField(
            value = state.player.name,
            label = "Name",
            isError = !state.isSavable,
            onValueChange = model::updateName
        )

        FormDropDownTextField(
            text = state.player.birthYear.toString(),
            label = "Birth year",
            readOnly = true,
            dropDownItems = (1900..2050).toList(),
            dropDownItemText = { Text(it.toString()) },
            onItemClicked = model::updateBirthYear
        )

        FormDropDownTextField(
            text = state.player.club,
            label = "Club",
            onTextChange = model::updateClub,
            dropDownItems = state.clubSuggestions,
            dropDownItemText = { Text(it) },
            onItemClicked = model::updateClub
        )
    }
}