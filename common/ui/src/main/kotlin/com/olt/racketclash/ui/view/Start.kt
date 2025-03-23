package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.state.start.StartModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.material.Link
import com.olt.racketclash.ui.material.Loading
import com.olt.racketclash.ui.layout.RacketClashScrollableScaffold

@Composable
internal fun Start(database: Database, navigateTo: (View) -> Unit) {
    val model = remember { StartModel(database = database) }
    val state by model.state.collectAsState()

    RacketClashScrollableScaffold(title = "Start") {
        Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
            ListPreviewBox(
                name = "Tournaments",
                isLoading = state.isLoading,
                items = state.tournaments,
                onNavigateMore = { navigateTo(View.Tournaments) }
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Link(it.name) { navigateTo(View.Tournament(tournamentName = it.name, tournamentId = it.id)) }
                    Text(
                        text = "(${it.startDateTime} to ${it.endDateTime})",
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )
                }
            }

            ListPreviewBox(
                name = "Players",
                isLoading = state.isLoading,
                items = state.players,
                onNavigateMore = { navigateTo(View.Players) }
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Link(it.name) { navigateTo(View.Player(playerName = it.name, playerId = it.id)) }
                    Text(
                        text = "(${it.club})",
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )
                }
            }

            ListPreviewBox(
                name = "Rules",
                isLoading = state.isLoading,
                items = state.rules,
                onNavigateMore = { navigateTo(View.Rules) }
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Link(it.name) { navigateTo(View.Rule(id = it.id)) }
                    Text(
                        text = "(used: ${it.used}, sets: ${it.winSets}/${it.maxSets}, points: ${it.winPoints}/${it.maxPoints} +/- ${it.pointsDifference}, rating: W:${it.gamePointsForWin} / D:${it.gamePointsForDraw} / L:${it.gamePointsForLose}, rest: G:${it.gamePointsForRest} / S:${it.setPointsForRest} / P:${it.pointPointsForRest})",
                        fontSize = MaterialTheme.typography.labelMedium.fontSize
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> ListPreviewBox(
    name: String,
    isLoading: Boolean,
    items: List<T>,
    onNavigateMore: () -> Unit,
    itemBuilder: @Composable (T) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 1.dp
    ) {
        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = name,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Box(modifier = Modifier.padding(10.dp)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (isLoading)
                        Loading()
                    else {
                        if (items.isEmpty())
                            Text("No items available")
                        else
                            items.forEach { itemBuilder(it) }
                    }

                    Link(
                        text = "more >>",
                        modifier = Modifier.align(Alignment.End),
                        onClick = onNavigateMore
                    )
                }
            }
        }
    }

}