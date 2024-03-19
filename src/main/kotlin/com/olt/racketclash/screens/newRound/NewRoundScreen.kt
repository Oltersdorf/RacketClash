package com.olt.racketclash.screens.newRound

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.TournamentScaffold
import com.olt.racketclash.ui.TournamentTabs

class NewRoundScreen(private val modelBuilder: () -> NewRoundModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "New Round",
            hasBackPress = true,
            selectedTab = TournamentTabs.Games,
            navigateTo = screenModel::navigateTo
        ) {
            NewRoundView(paddingValues = it, addRound = screenModel::addRound, navigateTo = screenModel::navigateTo)
        }
    }
}

private sealed class RoundType(val name: String) {
    data object Empty : RoundType("Empty")
    data object EquallyDouble : RoundType("Equally strong doubles")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewRoundView(
    paddingValues: PaddingValues,
    addRound: (String) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
    ) {
        Box(contentAlignment = Alignment.Center) {
            val verticalScrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .requiredWidthIn(500.dp)
                    .fillMaxWidth(0.5f)
                    .verticalScroll(verticalScrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var name by remember { mutableStateOf("") }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )

                var expanded by remember { mutableStateOf(false) }
                var selectedRoundType by remember { mutableStateOf<RoundType>(RoundType.Empty) }
                ExposedDropdownMenuBox(
                    modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        readOnly = true,
                        value = selectedRoundType.name,
                        onValueChange = {},
                        label = { Text("Team") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf(RoundType.Empty, RoundType.EquallyDouble).forEach {
                            DropdownMenuItem(
                                text = { Text(text = it.name) },
                                onClick = {
                                    selectedRoundType = it
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                when (selectedRoundType) {
                    RoundType.Empty -> {}
                    RoundType.EquallyDouble -> EquallyStrongDouble()
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val navigator = LocalNavigator.currentOrThrow

                    Spacer(modifier = Modifier.weight(1.0f))
                    Button(onClick = { navigateTo(Screens.Games, navigator) }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        addRound(name)
                        navigateTo(Screens.Games, navigator)
                    }) {
                        Text("Save")
                    }
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = verticalScrollState),
                style = LocalScrollbarStyle.current.copy(hoverColor = MaterialTheme.colorScheme.secondary, unhoverColor = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

@Composable
private fun EquallyStrongDouble() {
    Column(
        modifier = Modifier.padding(bottom = 50.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledIconButton(
                onClick = {},
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Remove"
                )
            }
            Text("1")
            FilledIconButton(
                onClick = {},
                enabled = true
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Add"
                )
            }
            Text(text = "Rounds")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.padding(start = 0.dp),
                checked = false,
                onCheckedChange = {}
            )
            Text("Different partners each round")
        }
    }
}