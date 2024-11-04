package com.olt.racketclash.ui.screen

import androidx.compose.runtime.Composable
import com.olt.racketclash.ui.layout.NavigationList
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun Tournament(
    tournamentId: Long,
    tournamentName: String,
    navigateTo: (Screens) -> Unit
) {
    NavigationList(
        navList = listOf(
            Screens.AddOrUpdateTournament(tournamentId = tournamentId, tournamentName = tournamentName),
            Screens.Teams(tournamentId = tournamentId),
            Screens.Categories(tournamentId = tournamentId),
            Screens.Schedule(tournamentId = tournamentId)
        ),
        onClick = navigateTo
    )
}