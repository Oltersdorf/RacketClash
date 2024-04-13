package com.olt.racketclash.screens.editPlayer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class EditPlayerScreen(private val modelBuilder: () -> EditPlayerModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            language = stateModel.language,
            topAppBarTitle = stateModel.language.editPlayer,
            hasBackPress = true,
            selectedTab = TournamentTabs.Players(language = stateModel.language),
            navigateTo = screenModel::navigateTo
        ) {
            SettingsView {
                EditPlayerView(
                    model = stateModel,
                    screenModel = screenModel
                )
            }
        }
    }
}

@Composable
private fun EditPlayerView(
    model: EditPlayerModel.Model,
    screenModel: EditPlayerModel
) {
    var playerName by remember { mutableStateOf(model.player?.name ?: "") }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = playerName,
        onValueChange = { playerName = it },
        label = { Text(model.language.name) },
        isError = playerName.isBlank()
    )

    DropDownMenu(
        modifier = Modifier.fillMaxWidth(),
        label = model.language.team,
        items = model.teams,
        value = model.selectedTeam,
        isError = model.selectedTeam.id == -1L,
        textMapper = { it.name },
        onClick = { screenModel.selectTeam(it.id) }
    )

    val navigator = LocalNavigator.currentOrThrow
    CancelSaveButtonRow(
        language = model.language,
        onCancel = { screenModel.navigateTo(Screens.Pop, navigator) },
        onSave = {
            screenModel.updatePlayer(model.player?.id, playerName, model.selectedTeam.id)
            screenModel.navigateTo(Screens.Pop, navigator)
        },
        canSave = playerName.isNotBlank() && model.selectedTeam.id != -1L
    )
}