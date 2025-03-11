package com.olt.racketclash.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.start.StartModel
import com.olt.racketclash.ui.Screens
import com.olt.racketclash.ui.component.Link
import com.olt.racketclash.ui.component.Loading

@Composable
internal fun Start(database: Database, navigateTo: (Screens) -> Unit) {
    val model = remember { StartModel(database = database) }
    val state by model.state.collectAsState()

    Column {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            shadowElevation = 5.dp
        ) {
            Text(
                text = "Start",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberScrollState()

            Column (
                modifier = Modifier.padding(50.dp).verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.spacedBy(50.dp)
            ) {
                ListPreviewBox(
                    name = "Tournaments",
                    isLoading = state.isLoading,
                    items = state.tournaments,
                    onNavigateMore = { navigateTo(Screens.Tournaments) }
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Link(it.name) { navigateTo(Screens.Tournament(tournamentName = it.name, tournamentId = it.id)) }
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
                    onNavigateMore = { navigateTo(Screens.Players) }
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Link(it.name) { navigateTo(Screens.Player(playerName = it.name, playerId = it.id)) }
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
                    onNavigateMore = { navigateTo(Screens.Rules) }
                ) {
                    Text(it.name)
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = scrollState),
                style = LocalScrollbarStyle.current.copy(
                    hoverColor = MaterialTheme.colorScheme.primary,
                    unhoverColor = MaterialTheme.colorScheme.primary,
                    shape = RectangleShape
                )
            )
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