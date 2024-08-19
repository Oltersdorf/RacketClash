package com.olt.racketclash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.editTeam.EditTeamModel
import com.olt.racketclash.language.Language
import com.olt.racketclash.navigate.Screens
import com.olt.racketclash.ui.*

@Composable
fun EditTeamScreen(
    model: EditTeamModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        projectId = state.projectId,
        topAppBarTitle = language.editTeam,
        hasBackPress = true,
        selectedTab = TournamentTabs.Teams(language = language, projectId = state.projectId),
        navigateTo = { navigateTo(it) }
    ) {
        SettingsView {
            EditTeamView(
                state = state,
                model = model,
                language = language,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
private fun EditTeamView(
    state: EditTeamModel.State,
    model: EditTeamModel,
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

    NumberSelector(
        label = language.strength + ":",
        value = state.strength,
        onValueChange = { model.changeStrength(newStrength = it) },
        min = 1
    )

    CancelSaveButtonRow(
        language = language,
        onCancel = { navigateTo(Screens.Pop) },
        onSave = {
            model.updateTeam()
            navigateTo(Screens.Pop)
        },
        canSave = state.name.isNotBlank()
    )
}