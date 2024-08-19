package com.olt.racketclash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.olt.racketclash.language.Language
import com.olt.racketclash.navigate.Screens
import com.olt.racketclash.newProject.NewProjectModel
import com.olt.racketclash.ui.CancelSaveButtonRow
import com.olt.racketclash.ui.SettingsView

@Composable
fun NewProjectScreen(
    model: NewProjectModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsView {
            Text(
                text = language.newProject,
                style = MaterialTheme.typography.headlineLarge
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.projectName,
                onValueChange = { model.changeProjectName(newName = it) },
                label = { Text(language.name) },
                isError = !state.canCreate
            )

            CancelSaveButtonRow(
                language = language,
                onCancel = { navigateTo(Screens.Pop) },
                onSave = {
                    model.addProject()
                    navigateTo(Screens.Pop)
                },
                canSave = state.canCreate
            )
        }
    }
}