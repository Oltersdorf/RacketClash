package com.olt.racketclash.screens.newProject

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.olt.racketclash.navigation.Screens

class NewProjectScreen(private val modelBuilder: () -> NewProjectModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .requiredWidthIn(min = 500.dp)
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "New Project",
                    fontSize = 50.sp,
                    modifier = Modifier.padding(top = 50.dp, bottom = 50.dp)
                )

                Spacer(modifier = Modifier.weight(1.0f))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = stateModel.projectName,
                    onValueChange = { screenModel.changeProjectName(newName = it) },
                    label = { Text("Name") },
                    isError = !stateModel.canCreate
                )

                Row(
                    modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var showDirPicker by remember { mutableStateOf(false) }

                    Text(text = "Location:", fontWeight = FontWeight.Bold)
                    Text(text = stateModel.location, modifier = Modifier.weight(1.0f))
                    Button(onClick = { showDirPicker = true }) {
                        Text("Change")
                    }

                    DirectoryPicker(show = showDirPicker, initialDirectory = System.getProperty("user.home")) { path ->
                        showDirPicker = false
                        screenModel.changeLocation(newLocation = path)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val navigator = LocalNavigator.currentOrThrow

                    Spacer(modifier = Modifier.weight(1.0f))
                    Button(onClick = { screenModel.navigateTo(screen = Screens.Pop, navigator = navigator) }) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            screenModel.addProject()
                            screenModel.navigateTo(screen = Screens.Pop, navigator = navigator)
                        },
                        enabled = stateModel.canCreate
                    ) {
                        Text("Create")
                    }
                }

                Spacer(modifier = Modifier.weight(1.0f))
            }
        }
    }
}