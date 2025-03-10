package com.olt.racketclash.ui.screen

import androidx.compose.runtime.Composable
import com.olt.racketclash.ui.layout.LinkNavigationList
import com.olt.racketclash.ui.Screens

@Composable
internal fun Tournament(
    tournamentId: Long,
    tournamentName: String,
    navigateTo: (Screens) -> Unit
) {
    LinkNavigationList(
        title = tournamentName,
        navList = listOf(
            Screens.Teams(tournamentId = tournamentId),
            Screens.Categories(tournamentId = tournamentId),
            Screens.Schedule(tournamentId = tournamentId)
        ),
        onClick = navigateTo,
        onLinkClick = {
            navigateTo(
                Screens.AddOrUpdateTournament(
                    tournamentId = tournamentId,
                    tournamentName = tournamentName
                )
            )
        }
    )
}