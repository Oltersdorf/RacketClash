package com.olt.racketclash.screens.editTeam

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class EditTeamScreen(private val modelBuilder: () -> EditTeamModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            language = stateModel.language,
            topAppBarTitle = stateModel.language.editTeam,
            hasBackPress = true,
            selectedTab = TournamentTabs.Teams(language = stateModel.language),
            navigateTo = screenModel::navigateTo
        ) {
            SettingsView {
                EditTeamView(
                    model = stateModel,
                    screenModel = screenModel
                )
            }
        }
    }
}

@Composable
private fun EditTeamView(
    model: EditTeamModel.Model,
    screenModel: EditTeamModel
) {
    var teamName by remember { mutableStateOf(model.team?.name ?: "") }
    var teamStrength by remember { mutableStateOf(model.team?.strength ?: 1) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = teamName,
        onValueChange = { teamName = it },
        label = { Text(model.language.name) },
        isError = teamName.isBlank()
    )

    NumberSelector(
        label = model.language.strength + ":",
        value = teamStrength,
        onValueChange = { teamStrength = it },
        min = 1
    )

    val navigator = LocalNavigator.currentOrThrow
    CancelSaveButtonRow(
        language = model.language,
        onCancel = { screenModel.navigateTo(Screens.Pop, navigator) },
        onSave = {
            screenModel.updateTeam(model.team?.id, teamName, teamStrength)
            screenModel.navigateTo(Screens.Pop, navigator)
        },
        canSave = teamName.isNotBlank()
    )
}