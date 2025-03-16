package com.olt.racketclash.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.screen.*
import com.olt.racketclash.ui.screen.Rules
import com.olt.racketclash.ui.screen.Players
import com.olt.racketclash.ui.screen.Start
import com.olt.racketclash.ui.screen.Tournaments

@Composable
internal fun Navigator(
    drawerState: DrawerState,
    database: Database
) {
    var currentScreen by remember { mutableStateOf<Screens>(Screens.Start) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape
            ) {
                Text(
                    text = "Racket Clash",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Start") },
                    selected = currentScreen is Screens.Start,
                    onClick = { currentScreen = Screens.Start }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Tournaments") },
                    selected = currentScreen is Screens.Tournaments,
                    onClick = { currentScreen = Screens.Tournaments }
                )
                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Players") },
                    selected = currentScreen is Screens.Players,
                    onClick = { currentScreen = Screens.Players }
                )
                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Rules") },
                    selected = currentScreen is Screens.Rules,
                    onClick = { currentScreen = Screens.Rules }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { }
                )

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("About") },
                    selected = false,
                    onClick = { }
                )
            }
        }
    ) {
        when (val cs = currentScreen) {
            is Screens.AddOrUpdateCategory -> AddOrUpdateCategory(database = database, categoryId = cs.categoryId, categoryName = cs.categoryName, tournamentId = cs.tournamentId) { currentScreen = Screens.Categories(tournamentId = cs.tournamentId) }
            is Screens.AddOrUpdateRule -> AddOrUpdateRule(database = database, ruleId = cs.ruleId, ruleName = cs.ruleName) { currentScreen = Screens.Rules }
            is Screens.AddSchedule -> AddSchedule(database = database, categoryId = cs.categoryId, categoryName = cs.categoryName, tournamentId = cs.tournamentId) { currentScreen = Screens.Schedule(tournamentId = cs.tournamentId) }
            is Screens.AddOrUpdatePlayer -> AddOrUpdatePlayer(database = database, playerId = cs.playerId, playerName = cs.playerName) { currentScreen = Screens.Players }
            is Screens.AddOrUpdateTeam -> AddOrUpdateTeam(database = database, teamId = cs.teamId, teamName = cs.teamName, tournamentId = cs.tournamentId) { currentScreen = Screens.Teams(tournamentId = cs.tournamentId) }
            is Screens.AddOrUpdateTournament -> AddOrUpdateTournament(database = database, tournamentId = cs.tournamentId, tournamentName = cs.tournamentName) { currentScreen = Screens.Tournaments }
            is Screens.Categories -> Categories(database = database, tournamentId = cs.tournamentId) { currentScreen = it }
            is Screens.Category -> Category(database = database, categoryId = cs.categoryId, categoryName = cs.categoryName, tournamentId = cs.tournamentId) { currentScreen = it }
            Screens.Rules -> Rules(database = database) { currentScreen = it }
            is Screens.Player -> Player(database = database, playerId = cs.playerId, playerName = cs.playerName) { currentScreen = it }
            Screens.Players -> Players(database = database) { currentScreen = it }
            Screens.Start -> Start(database = database) { currentScreen = it }
            is Screens.Schedule -> Schedule(database = database, tournamentId = cs.tournamentId)
            is Screens.Team -> Team(database = database, teamId = cs.teamId, teamName = cs.teamName, tournamentId = cs.tournamentId) { currentScreen = it }
            is Screens.Teams -> Teams(database = database, tournamentId = cs.tournamentId) { currentScreen = it }
            is Screens.Tournament -> Tournament(tournamentId = cs.tournamentId, tournamentName = cs.tournamentName) { currentScreen = it }
            Screens.Tournaments -> Tournaments(database = database) { currentScreen = it }
        }
    }
}