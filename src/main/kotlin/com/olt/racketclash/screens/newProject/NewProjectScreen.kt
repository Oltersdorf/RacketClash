package com.olt.racketclash.screens.newProject

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

class NewProjectScreen(
    private val projectNames: List<String>,
    private val addProject: (name: String, location: Path) -> Unit
) : Screen {

    @Composable
    override fun Content() {
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

                var projectName by remember { mutableStateOf("") }
                var location by remember { mutableStateOf(Path(System.getProperty("user.home"))) }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = projectName,
                    onValueChange = {
                        projectName = it
                    },
                    label = { Text("Name") },
                    isError = projectName.isBlank() || projectNames.contains(projectName)
                )

                Row(
                    modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var showDirPicker by remember { mutableStateOf(false) }

                    Text(text = "Location:", fontWeight = FontWeight.Bold)
                    Text(text = location.absolutePathString(), modifier = Modifier.weight(1.0f))
                    Button(onClick = { showDirPicker = true }) {
                        Text("Change")
                    }

                    DirectoryPicker(show = showDirPicker, initialDirectory = System.getProperty("user.home")) { path ->
                        showDirPicker = false
                        if (path != null)
                            location = Path(path)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val navigator = LocalNavigator.currentOrThrow

                    Spacer(modifier = Modifier.weight(1.0f))
                    Button(onClick = { navigator.pop() }) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            addProject(projectName, location)
                            navigator.pop()
                        },
                        enabled = projectName.isNotBlank() && !projectNames.contains(projectName)
                    ) {
                        Text("Create")
                    }
                }

                Spacer(modifier = Modifier.weight(1.0f))
            }
        }
    }
}