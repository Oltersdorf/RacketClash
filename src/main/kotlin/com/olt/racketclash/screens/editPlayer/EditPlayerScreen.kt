package com.olt.racketclash.screens.editPlayer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Player
import com.olt.racketclash.data.Team
import com.olt.racketclash.screens.players.updatePlayer
import com.olt.racketclash.ui.TournamentScaffold
import com.olt.racketclash.ui.TournamentTabs

class EditPlayerScreen(
    private val player: Player?,
    private val teams: List<Team>,
    private val updatePlayer: updatePlayer
) : Screen {

    @Composable
    override fun Content() {
        TournamentScaffold(
            topAppBarTitle = "Edit Player",
            hasBackPress = true,
            selectedTab = TournamentTabs.Player
        ) {
            EditPlayerView(paddingValues = it, player = player, teams = teams, updatePlayer = updatePlayer)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPlayerView(
    paddingValues: PaddingValues,
    player: Player?,
    teams: List<Team>,
    updatePlayer: updatePlayer
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

            var expanded by remember { mutableStateOf(false) }
            var selectedTeam by remember { mutableStateOf(teams.find { it.id == player?.id } ?: Team(name = "<No Team Selected>", id = -1L, strength = 0, size = 0)) }

            ExposedDropdownMenuBox(
                modifier = Modifier.padding(top = 50.dp, bottom = 50.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = selectedTeam.name,
                    onValueChange = {},
                    label = { Text("Team") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    isError = selectedTeam.id == -1L
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    teams.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.name) },
                            onClick = {
                                selectedTeam = it
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
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
                        updatePlayer(player?.id, playerName, selectedTeam.id)
                        navigator.pop()
                    },
                    enabled = playerName.isNotBlank() && selectedTeam.id != -1L
                ) {
                    Text("Save")
                }
            }
        }
    }
}