package com.olt.racketclash.ui

import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.screen.*
import com.olt.racketclash.ui.screen.Rules
import com.olt.racketclash.ui.screen.Players
import com.olt.racketclash.ui.screen.Start
import com.olt.racketclash.ui.screen.Tournaments

@Composable
internal fun Navigator(
    database: Database
) {
    var navLinks by remember { mutableStateOf(listOf<Screens>(Screens.RacketClash)) }

    when (val navLink = navLinks.lastOrNull()) {
        is Screens.AddOrUpdateCategory -> AddOrUpdateCategory(database = database, categoryId = navLink.categoryId, categoryName = navLink.categoryName, tournamentId = navLink.tournamentId) { navLinks = navLinks.dropLast(1) }
        is Screens.AddOrUpdateRule -> AddOrUpdateRule(database = database, ruleId = navLink.ruleId, ruleName = navLink.ruleName) { navLinks = navLinks.dropLast(1) }
        is Screens.AddSchedule -> AddSchedule(database = database, categoryId = navLink.categoryId, categoryName = navLink.categoryName, tournamentId = navLink.tournamentId) { navLinks = navLinks.dropLast(1) }
        is Screens.AddOrUpdatePlayer -> AddOrUpdatePlayer(database = database, playerId = navLink.playerId, playerName = navLink.playerName) { navLinks = navLinks.dropLast(1) }
        is Screens.AddOrUpdateTeam -> AddOrUpdateTeam(database = database, teamId = navLink.teamId, teamName = navLink.teamName, tournamentId = navLink.tournamentId) { navLinks = navLinks.dropLast(1) }
        is Screens.AddOrUpdateTournament -> AddOrUpdateTournament(database = database, tournamentId = navLink.tournamentId, tournamentName = navLink.tournamentName) { navLinks = navLinks.dropLast(1) }
        is Screens.Categories -> Categories(database = database, tournamentId = navLink.tournamentId) { screen -> navLinks += screen }
        is Screens.Category -> Category(database = database, categoryId = navLink.categoryId, categoryName = navLink.categoryName, tournamentId = navLink.tournamentId) { screen -> navLinks += screen }
        Screens.Rules -> Rules(database = database) { screen -> navLinks += screen }
        is Screens.Player -> Player(database = database, playerId = navLink.playerId, playerName = navLink.playerName) { screen -> navLinks += screen }
        Screens.Players -> Players(database = database) { screen -> navLinks += screen }
        Screens.RacketClash -> Start(database = database) { screen -> navLinks += screen }
        is Screens.Schedule -> Schedule(database = database, tournamentId = navLink.tournamentId)
        is Screens.Team -> Team(database = database, teamId = navLink.teamId, teamName = navLink.teamName, tournamentId = navLink.tournamentId) { screen -> navLinks += screen }
        is Screens.Teams -> Teams(database = database, tournamentId = navLink.tournamentId) { screen -> navLinks += screen }
        is Screens.Tournament -> Tournament(tournamentId = navLink.tournamentId, tournamentName = navLink.tournamentName) { screen -> navLinks += screen }
        Screens.Tournaments -> Tournaments(database = database) { screen -> navLinks += screen }
        null -> { navLinks = listOf(Screens.RacketClash) }
    }
}