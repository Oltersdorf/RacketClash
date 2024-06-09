package com.olt.racketclash.app.screens.editPlayer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.app.Screens
import com.olt.racketclash.language.Language
import com.olt.racketclash.ui.*

@Composable
fun EditPlayerScreen(
    model: EditPlayerModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        topAppBarTitle = language.editPlayer,
        hasBackPress = true,
        selectedTab = TournamentTabs.Players(language = language),
        navigateTo = { navigateTo(it) }
    ) {
        SettingsView {
            EditPlayerView(
                state = state,
                model = model,
                language = language,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
private fun EditPlayerView(
    state: EditPlayerModel.State,
    model: EditPlayerModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.name,
        onValueChange = { model.changeName(newName = it) },
        label = { Text(language.name) },
        isError = state.name.isBlank()
    )

    DropDownMenu(
        modifier = Modifier.fillMaxWidth(),
        label = language.team,
        items = state.teams,
        value = state.selectedTeam,
        isError = state.selectedTeam.id == -1L,
        textMapper = { it.name },
        onClick = { model.selectTeam(it.id) }
    )

    CancelSaveButtonRow(
        language = language,
        onCancel = { navigateTo(Screens.Pop) },
        onSave = {
            model.updatePlayer()
            navigateTo(Screens.Pop)
        },
        canSave = state.name.isNotBlank() && state.selectedTeam.id != -1L
    )
}