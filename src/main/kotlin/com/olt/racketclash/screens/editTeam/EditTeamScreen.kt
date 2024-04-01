package com.olt.racketclash.screens.editTeam

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.CancelSaveButtonRow
import com.olt.racketclash.ui.TournamentScaffold
import com.olt.racketclash.ui.TournamentTabs

class EditTeamScreen(private val modelBuilder: () -> EditTeamModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Edit Team",
            hasBackPress = true,
            selectedTab = TournamentTabs.Teams,
            navigateTo = screenModel::navigateTo
        ) {
            EditTeamView(
                paddingValues = it,
                team = stateModel.team,
                updateTeam = screenModel::updateTeam,
                navigateTo = screenModel::navigateTo
            )
        }
    }
}

@Composable
private fun EditTeamView(
    paddingValues: PaddingValues,
    team: Team?,
    updateTeam: (Long?, String, Int) -> Unit,
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

            val navigator = LocalNavigator.currentOrThrow
            CancelSaveButtonRow(
                onCancel = { navigateTo(Screens.Pop, navigator) },
                onSave = {
                    updateTeam(team?.id, teamName, teamStrength.toInt())
                    navigateTo(Screens.Pop, navigator)
                },
                canSave = teamName.isNotBlank() && teamStrength.isNotEmpty()
            )
        }
    }
}