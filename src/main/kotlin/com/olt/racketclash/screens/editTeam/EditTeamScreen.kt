package com.olt.racketclash.screens.editTeam

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

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
            SettingsView {
                EditTeamView(
                    team = stateModel.team,
                    updateTeam = screenModel::updateTeam,
                    navigateTo = screenModel::navigateTo
                )
            }
        }
    }
}

@Composable
private fun EditTeamView(
    team: Team?,
    updateTeam: (Long?, String, Int) -> Unit,
    navigateTo: (Screens, Navigator) -> Unit
) {
    var teamName by remember { mutableStateOf(team?.name ?: "") }
    var teamStrength by remember { mutableStateOf(team?.strength ?: 1) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = teamName,
        onValueChange = { teamName = it },
        label = { Text("Name") },
        isError = teamName.isBlank()
    )

    NumberSelector(
        label = "Difficulty:",
        value = teamStrength,
        onValueChange = { teamStrength = it },
        min = 1
    )

    val navigator = LocalNavigator.currentOrThrow
    CancelSaveButtonRow(
        onCancel = { navigateTo(Screens.Pop, navigator) },
        onSave = {
            updateTeam(team?.id, teamName, teamStrength)
            navigateTo(Screens.Pop, navigator)
        },
        canSave = teamName.isNotBlank()
    )
}