package com.olt.racketclash.screens.newRound

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.olt.racketclash.ui.*

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

                var selectedRoundType by remember { mutableStateOf<RoundType>(RoundType.Empty) }
                DropDownMenu(
                    modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                    label = "Team",
                    items = listOf(RoundType.Empty, RoundType.EquallyDouble),
                    value = selectedRoundType.name,
                    textMapper = { it.name },
                    onClick = { selectedRoundType = it }
                )

                when (selectedRoundType) {
                    RoundType.Empty -> {}
                    RoundType.EquallyDouble -> EquallyStrongDouble()
                }

                val navigator = LocalNavigator.currentOrThrow
                CancelSaveButtonRow(
                    onCancel = { navigateTo(Screens.Games, navigator) },
                    onSave = {
                        addRound(name)
                        navigateTo(Screens.Games, navigator)
                    }
                )
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
            FilledArrowLeftButton(enabled = true) {}
            Text("1")
            FilledArrowRightButton(enabled = true) {}
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