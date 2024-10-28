package com.olt.racketclash.ui.navigate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.Link

@Composable
fun Navigator(database: Database) {
    Column {
        var navLinks by remember { mutableStateOf(listOf<Screens>(Screens.RacketClash)) }

        LinkRow(navLinks = navLinks) { navLinks = navLinks.subList(0, it + 1) }

        when (navLinks.lastOrNull()) {
            is Screens.AddOrUpdateCategory -> TODO()
            is Screens.AddOrUpdateGameRule -> TODO()
            is Screens.AddOrUpdateGames -> TODO()
            is Screens.AddOrUpdatePlayer -> TODO()
            is Screens.AddOrUpdateTeam -> TODO()
            is Screens.AddOrUpdateTournament -> TODO()
            is Screens.Categories -> TODO()
            is Screens.Category -> TODO()
            is Screens.GamRules -> TODO()
            is Screens.Players -> TODO()
            Screens.RacketClash -> TODO()
            is Screens.Team -> TODO()
            is Screens.Teams -> TODO()
            is Screens.Tournament -> TODO()
            is Screens.Tournaments -> TODO()
            null -> { navLinks = listOf(Screens.RacketClash) }
        }
    }
}

@Composable
private fun LinkRow(
    navLinks: List<Screens>,
    onLinkClicked: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        navLinks.forEachIndexed { index, screen ->
            Link(text = screen.name, fontSize = MaterialTheme.typography.titleLarge.fontSize) {
                onLinkClicked(index)
            }

            if (index + 1 < navLinks.size)
                Text(
                    text = "/",
                    modifier = Modifier.padding(horizontal = 5.dp),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
        }
    }
}