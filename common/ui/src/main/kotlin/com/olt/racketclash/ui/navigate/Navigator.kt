package com.olt.racketclash.ui.navigate

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.DropDownIconButton
import com.olt.racketclash.ui.component.SimpleIconButton
import com.olt.racketclash.ui.screen.GameRules
import com.olt.racketclash.ui.screen.Players
import com.olt.racketclash.ui.screen.RacketClash
import com.olt.racketclash.ui.screen.Tournaments

@Composable
fun Navigator(
    isDarkMode: Boolean,
    switchDarkMode: () -> Unit,
    database: Database
) {
    var navLinks by remember { mutableStateOf(listOf<Screens>(Screens.RacketClash)) }

    Scaffold(
        topBar = {
            TopBar(
                navLinks = navLinks,
                isDarkMode = isDarkMode,
                switchDarkMode = switchDarkMode
            ) {
                val index = navLinks.indexOf(it)
                if (index + 1 != navLinks.size && index > -1)
                    navLinks = navLinks.subList(0, index + 1)
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            when (navLinks.lastOrNull()) {
                is Screens.AddOrUpdateCategory -> TODO()
                is Screens.AddOrUpdateGameRule -> TODO()
                is Screens.AddOrUpdateGames -> TODO()
                is Screens.AddOrUpdatePlayer -> TODO()
                is Screens.AddOrUpdateTeam -> TODO()
                is Screens.AddOrUpdateTournament -> TODO()
                is Screens.Categories -> TODO()
                is Screens.Category -> TODO()
                Screens.GamRules -> GameRules(database = database) { screen -> navLinks += screen }
                is Screens.Player -> TODO()
                Screens.Players -> Players(database = database) { screen -> navLinks += screen }
                Screens.RacketClash -> RacketClash { screen -> navLinks += screen }
                is Screens.Team -> TODO()
                is Screens.Teams -> TODO()
                is Screens.Tournament -> TODO()
                Screens.Tournaments -> TODO()
                null -> { navLinks = listOf(Screens.RacketClash) }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    navLinks: List<Screens>,
    isDarkMode: Boolean,
    switchDarkMode: () -> Unit,
    onClick: (Screens) -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        navigationIcon = {
            DropDownIconButton(
                items = navLinks.dropLast(1),
                textMapper = { it.name },
                onClick = onClick
            )
        },
        title = { Text(navLinks.last().name) },
        actions = {
            SimpleIconButton(
                painter = if (isDarkMode)
                    painterResource(resourcePath = "dark_mode.svg")
                else
                    painterResource("light_mode.svg"),
                contentDescription = "Light/Dark mode",
                onClick = switchDarkMode
            )
        }
    )
}