package com.olt.racketclash.screens.editRound

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.olt.racketclash.ui.LazyColumnWithScroll
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
                paddingValues = it,
                model = stateModel
            )
        }
    }
}

@Composable
private fun EditRoundView(
    paddingValues: PaddingValues,
    model: EditRoundModel.Model
) {
    Surface(modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)) {
        Column(
            modifier = Modifier.requiredWidthIn(500.dp).fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var roundName by remember { mutableStateOf("roundName") }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = roundName,
                onValueChange = { roundName = it },
                label = { Text("Name") }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Games")
                Spacer(modifier = Modifier.weight(1.0f))
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }

            LazyColumnWithScroll(
                header = {
                    Surface(tonalElevation = 1.dp) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Left",
                                modifier = Modifier.weight(5.0f)
                            )
                            Text(
                                text = "Results",
                                modifier = Modifier.weight(1.0f)
                            )
                            Text(
                                text = "Right",
                                modifier = Modifier.weight(5.0f)
                            )
                            Text(
                                text = "Delete",
                                modifier = Modifier.weight(1.0f)
                            )
                        }
                    }
                }
            ) {
                items(items = model.games) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(5.0f)) {
                            Text(it.playerLeft1Name ?: "")
                            Text(it.playerLeft2Name ?: "")
                        }
                        Column(modifier = Modifier.weight(1.0f)) {
                            BasicTextField(
                                value = "0:0",
                                onValueChange = {},
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.primary).widthIn(max = 20.dp)
                            )
                            BasicTextField(
                                value = "0:0",
                                onValueChange = {},
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.primary).widthIn(max = 20.dp)
                            )
                            BasicTextField(
                                value = "0:0",
                                onValueChange = {},
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.primary).widthIn(max = 20.dp)
                            )
                            BasicTextField(
                                value = "0:0",
                                onValueChange = {},
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.primary).widthIn(max = 20.dp)
                            )
                            BasicTextField(
                                value = "0:0",
                                onValueChange = {},
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.primary).widthIn(max = 20.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(5.0f)) {
                            Text(it.playerRight1Name ?: "")
                            Text(it.playerRight2Name ?: "")
                        }
                        IconButton(
                            modifier = Modifier.weight(1.0f),
                            onClick = {  }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
}