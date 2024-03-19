package com.olt.racketclash.screens.editRound

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.olt.racketclash.ui.LazyTableColumn
import com.olt.racketclash.ui.LazyTableWithScroll
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

            LazyTableWithScroll(
                items = model.games,
                columns = listOf(
                    LazyTableColumn(
                        name = "Left",
                        weight = 5.0f
                    ) { item, weight ->
                        Column(modifier = Modifier.weight(weight)) {
                            Text(item.playerLeft1Name ?: "")
                            Text(item.playerLeft2Name ?: "")
                        }
                    },
                    LazyTableColumn(
                        name = "Results",
                        weight = 1.0f
                    ) { item, weight ->
                        Column(modifier = Modifier.weight(weight)) {
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
                    },
                    LazyTableColumn(
                        name = "Right",
                        weight = 5.0f
                    ) { item, weight ->
                        Column(modifier = Modifier.weight(weight)) {
                            Text(item.playerRight1Name ?: "")
                            Text(item.playerRight2Name ?: "")
                        }
                    },
                    LazyTableColumn(
                        name = "Delete",
                        weight = 1.0f,
                        textAlign = TextAlign.Center
                    ) { item, weight ->
                        IconButton(
                            modifier = Modifier.weight(weight),
                            onClick = {  }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                )
            )
        }
    }
}