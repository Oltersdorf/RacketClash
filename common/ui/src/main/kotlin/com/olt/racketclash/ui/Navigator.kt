package com.olt.racketclash.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.view.Players
import com.olt.racketclash.ui.view.*
import com.olt.racketclash.ui.view.AddOrUpdatePlayer
import com.olt.racketclash.ui.view.AddSchedule
import com.olt.racketclash.ui.view.Player
import com.olt.racketclash.ui.view.Rules
import com.olt.racketclash.ui.view.Start
import com.olt.racketclash.ui.view.Tournaments

@Composable
internal fun Navigator(
    drawerState: DrawerState,
    database: Database
) {
    var currentView by remember { mutableStateOf<View>(View.Start) }

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
                    selected = currentView is View.Start,
                    onClick = { currentView = View.Start }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Tournaments") },
                    selected = currentView is View.Tournaments,
                    onClick = { currentView = View.Tournaments }
                )
                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Players") },
                    selected = currentView is View.Players,
                    onClick = { currentView = View.Players }
                )
                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Rules") },
                    selected = currentView is View.Rules,
                    onClick = { currentView = View.Rules }
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
        when (val cv = currentView) {
            is View.AddOrUpdateCategory -> AddOrUpdateCategory(database = database, categoryId = cv.categoryId, categoryName = cv.categoryName, tournamentId = cv.tournamentId) { currentView = View.Categories(tournamentId = cv.tournamentId) }
            is View.AddOrUpdateRule -> AddOrUpdateRule(database = database, ruleId = cv.ruleId, ruleName = cv.ruleName) { currentView = View.Rules }
            is View.AddSchedule -> AddSchedule(database = database, categoryId = cv.categoryId, categoryName = cv.categoryName, tournamentId = cv.tournamentId) { currentView = View.Schedule(tournamentId = cv.tournamentId) }
            is View.AddOrUpdatePlayer -> AddOrUpdatePlayer(database = database, playerId = cv.playerId, playerName = cv.playerName) { currentView = View.Players }
            is View.AddOrUpdateTeam -> AddOrUpdateTeam(database = database, teamId = cv.teamId, teamName = cv.teamName, tournamentId = cv.tournamentId) { currentView = View.Teams(tournamentId = cv.tournamentId) }
            is View.AddOrUpdateTournament -> AddOrUpdateTournament(database = database, tournamentId = cv.tournamentId, tournamentName = cv.tournamentName) { currentView = View.Tournaments }
            is View.Categories -> Categories(database = database, tournamentId = cv.tournamentId) { currentView = it }
            is View.Category -> Category(database = database, categoryId = cv.categoryId, categoryName = cv.categoryName, tournamentId = cv.tournamentId) { currentView = it }
            View.Rules -> Rules(database = database) { currentView = it }
            is View.Player -> Player(database = database, playerId = cv.playerId, playerName = cv.playerName) { currentView = it }
            View.Players -> Players(database = database) { currentView = it }
            View.Start -> Start(database = database) { currentView = it }
            is View.Schedule -> Schedule(database = database, tournamentId = cv.tournamentId)
            is View.Team -> Team(database = database, teamId = cv.teamId, teamName = cv.teamName, tournamentId = cv.tournamentId) { currentView = it }
            is View.Teams -> Teams(database = database, tournamentId = cv.tournamentId) { currentView = it }
            is View.Tournament -> Tournament(tournamentId = cv.tournamentId, tournamentName = cv.tournamentName) { currentView = it }
            View.Tournaments -> Tournaments(database = database) { currentView = it }
        }
    }
}