package com.olt.racketclash.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Team

class EditTeamScreen(
    private val team: Team?,
    private val updateTeam: updateTeam
) : Screen {

    @Composable
    override fun Content() {
        TournamentScaffold(
            topAppBarTitle = "Edit Team",
            hasBackPress = true,
            selectedTab = TournamentTabs.Teams
        ) {
            EditTeamView(
                paddingValues = it,
                team = team,
                updateTeam = updateTeam
            )
        }
    }
}

@Composable
private fun EditTeamView(
    paddingValues: PaddingValues,
    team: Team?,
    updateTeam: updateTeam
) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
    ) {
        Column(
            modifier = Modifier.requiredWidthIn(500.dp).fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var teamName by remember { mutableStateOf(team?.name ?: "") }
            var teamStrength by remember { mutableStateOf(team?.strength?.toString() ?: "1") }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = teamName,
                onValueChange = { teamName = it },
                label = { Text("Name") },
                isError = teamName.isBlank()
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 50.dp, bottom = 50.dp),
                value = teamStrength,
                onValueChange = {
                    val strength = it.toIntOrNull()
                    if (it.isEmpty() || (strength != null && strength > 0))
                        teamStrength = it
                },
                label = { Text("Difficulty") },
                isError = teamStrength.isEmpty()
            )
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
                        updateTeam(team?.id, teamName, teamStrength.toInt())
                        navigator.pop()
                    },
                    enabled = teamName.isNotBlank() && teamStrength.isNotEmpty()
                ) {
                    Text("Save")
                }
            }
        }
    }
}