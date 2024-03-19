package com.olt.racketclash.screens.editRound

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.olt.racketclash.ui.TournamentScaffold
import com.olt.racketclash.ui.TournamentTabs

class EditRoundScreen(private val modelBuilder: () -> EditRoundModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Edit Round",
            hasBackPress = true,
            selectedTab = TournamentTabs.Games,
            navigateTo = screenModel::navigateTo
        ) {
            EditRoundView(
                paddingValues = it
            )
        }
    }
}

@Composable
private fun EditRoundView(
    paddingValues: PaddingValues
) {
    Surface(modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var roundName by remember { mutableStateOf("roundName") }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = roundName,
                onValueChange = { roundName = it },
                label = { Text("Name") }
            )
        }
    }
}