package com.olt.racketclash.screens.editGame

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.olt.racketclash.ui.TournamentScaffold
import com.olt.racketclash.ui.TournamentTabs

class EditGameScreen(private val modelBuilder: () -> EditGameModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "Edit Game",
            hasBackPress = true,
            selectedTab = TournamentTabs.Games,
            navigateTo = screenModel::navigateTo
        ) {
            EditGameView(
                paddingValues = it,
                model = stateModel
            )
        }
    }
}

@Composable
private fun EditGameView(
    paddingValues: PaddingValues,
    model: EditGameModel.Model
) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
    ) {
        Column(
            modifier = Modifier.requiredWidthIn(500.dp).fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = model.roundName, style = MaterialTheme.typography.titleLarge)
        }
    }
}