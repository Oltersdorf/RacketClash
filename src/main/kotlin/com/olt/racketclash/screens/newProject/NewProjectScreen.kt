package com.olt.racketclash.screens.newProject

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.CancelSaveButtonRow
import com.olt.racketclash.ui.SettingsView

class NewProjectScreen(private val modelBuilder: () -> NewProjectModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsView {
                Text(
                    text = stateModel.language.newProject,
                    style = MaterialTheme.typography.headlineLarge
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = stateModel.projectName,
                    onValueChange = { screenModel.changeProjectName(newName = it) },
                    label = { Text(stateModel.language.name) },
                    isError = !stateModel.canCreate
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var showDirPicker by remember { mutableStateOf(false) }

                    Text(text = stateModel.language.saveLocation + ":", fontWeight = FontWeight.Bold)
                    Text(text = stateModel.location, modifier = Modifier.weight(1.0f))
                    Button(onClick = { showDirPicker = true }) {
                        Text(stateModel.language.change)
                    }

                    DirectoryPicker(show = showDirPicker, initialDirectory = System.getProperty("user.home")) { path ->
                        showDirPicker = false
                        screenModel.changeLocation(newLocation = path)
                    }
                }

                val navigator = LocalNavigator.currentOrThrow
                CancelSaveButtonRow(
                    language = stateModel.language,
                    onCancel = { screenModel.navigateTo(screen = Screens.Pop, navigator = navigator) },
                    onSave = {
                        screenModel.addProject()
                        screenModel.navigateTo(screen = Screens.Pop, navigator = navigator)
                    },
                    canSave = stateModel.canCreate
                )
            }
        }
    }
}