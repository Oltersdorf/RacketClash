package com.olt.racketclash.ui.navigate

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.DropDownIconButton
import com.olt.racketclash.ui.component.SimpleIconButton
import com.olt.racketclash.ui.screen.*
import com.olt.racketclash.ui.screen.GameRules
import com.olt.racketclash.ui.screen.Players
import com.olt.racketclash.ui.screen.RacketClash
import com.olt.racketclash.ui.screen.Tournaments
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.generated.resources.Res
import racketclash.common.ui.generated.resources.dark_mode
import racketclash.common.ui.generated.resources.light_mode

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
            when (val navLink = navLinks.lastOrNull()) {
                is Screens.AddOrUpdateCategory -> AddOrUpdateCategory(database = database, categoryId = navLink.categoryId, categoryName = navLink.categoryName, tournamentId = navLink.tournamentId) { navLinks = navLinks.dropLast(1) }
                is Screens.AddOrUpdateGameRule -> AddOrUpdateGameRule(database = database, gameRuleId = navLink.gameRuleId, gameRuleName = navLink.gameRuleName) { navLinks = navLinks.dropLast(1) }
                is Screens.AddOrUpdateGames -> TODO()
                is Screens.AddOrUpdatePlayer -> AddOrUpdatePlayer(database = database, playerId = navLink.playerId, playerName = navLink.playerName) { navLinks = navLinks.dropLast(1) }
                is Screens.AddOrUpdateTeam -> AddOrUpdateTeam(database = database, teamId = navLink.teamId, teamName = navLink.teamName, tournamentId = navLink.tournamentId) { navLinks = navLinks.dropLast(1) }
                is Screens.AddOrUpdateTournament -> AddOrUpdateTournament(database = database, tournamentId = navLink.tournamentId, tournamentName = navLink.tournamentName) { navLinks = navLinks.dropLast(1) }
                is Screens.Categories -> Categories(database = database, tournamentId = navLink.tournamentId) { screen -> navLinks += screen }
                is Screens.Category -> TODO()
                Screens.GameRules -> GameRules(database = database) { screen -> navLinks += screen }
                is Screens.Player -> Player(database = database, playerId = navLink.playerId, playerName = navLink.playerName) { screen -> navLinks += screen }
                Screens.Players -> Players(database = database) { screen -> navLinks += screen }
                Screens.RacketClash -> RacketClash { screen -> navLinks += screen }
                is Screens.Schedule -> Schedule(database = database, tournamentId = navLink.tournamentId)
                is Screens.Team -> Team(database = database, teamId = navLink.teamId, teamName = navLink.teamName, tournamentId = navLink.tournamentId) { screen -> navLinks += screen }
                is Screens.Teams -> Teams(database = database, tournamentId = navLink.tournamentId) { screen -> navLinks += screen }
                is Screens.Tournament -> Tournament(tournamentId = navLink.tournamentId, tournamentName = navLink.tournamentName) { screen -> navLinks += screen }
                Screens.Tournaments -> Tournaments(database = database) { screen -> navLinks += screen }
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
                    painterResource(Res.drawable.light_mode)
                else
                    painterResource(Res.drawable.dark_mode),
                contentDescription = "Light/Dark mode",
                onClick = switchDarkMode
            )
        }
    )
}