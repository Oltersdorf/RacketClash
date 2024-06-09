package com.olt.racketclash.app.screens.newProject

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.olt.racketclash.app.Screens
import com.olt.racketclash.language.Language
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var showDirPicker by remember { mutableStateOf(false) }

                Text(text = language.saveLocation + ":", fontWeight = FontWeight.Bold)
                Text(text = state.location, modifier = Modifier.weight(1.0f))
                Button(onClick = { showDirPicker = true }) {
                    Text(language.change)
                }

                DirectoryPicker(show = showDirPicker, initialDirectory = System.getProperty("user.home")) { path ->
                    showDirPicker = false
                    model.changeLocation(newLocation = path)
                }
            }

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