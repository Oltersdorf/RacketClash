package com.olt.racketclash.screens.editPlayer

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
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.DropDownMenu
import com.olt.racketclash.ui.TournamentScaffold
import com.olt.racketclash.ui.TournamentTabs

class EditPlayerScreen(private val modelBuilder: () -> EditPlayerModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Edit Player",
            hasBackPress = true,
            selectedTab = TournamentTabs.Players,
            navigateTo = screenModel::navigateTo
        ) {
            EditPlayerView(
                paddingValues = it,
                player = stateModel.player,
                teams = stateModel.teams,
                selectedTeam = stateModel.selectedTeam,
                updatePlayer = screenModel::updatePlayer,
                selectTeam = screenModel::selectTeam,
                navigateTo = screenModel::navigateTo
            )
        }
    }
}

@Composable
private fun EditPlayerView(
    paddingValues: PaddingValues,
    player: Player?,
    teams: List<Team>,
    selectedTeam: Team,
    updatePlayer: (Long?, String, Long) -> Unit,
    selectTeam: (Long) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
    ) {
        Column(
            modifier = Modifier.requiredWidthIn(500.dp).fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var playerName by remember { mutableStateOf(player?.name ?: "") }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Name") },
                isError = playerName.isBlank()
            )

            DropDownMenu(
                modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                label = "Team",
                items = teams,
                value = selectedTeam.name,
                isError = selectedTeam.id == -1L,
                textMapper = { it.name },
                onClick = { selectTeam(it.id) }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val navigator = LocalNavigator.currentOrThrow

                Spacer(modifier = Modifier.weight(1.0f))
                Button(onClick = { navigateTo(Screens.Pop, navigator) }) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        updatePlayer(player?.id, playerName, selectedTeam.id)
                        navigateTo(Screens.Pop, navigator)
                    },
                    enabled = playerName.isNotBlank() && selectedTeam.id != -1L
                ) {
                    Text("Save")
                }
            }
        }
    }
}