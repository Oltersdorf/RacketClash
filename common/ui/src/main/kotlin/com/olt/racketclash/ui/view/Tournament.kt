package com.olt.racketclash.ui.view

import androidx.compose.runtime.Composable
import com.olt.racketclash.ui.layout.LinkNavigationList
import com.olt.racketclash.ui.View

@Composable
internal fun Tournament(
    tournamentId: Long,
    tournamentName: String,
    navigateTo: (View) -> Unit
) {
    LinkNavigationList(
        title = tournamentName,
        navList = listOf(
            View.Teams(tournamentId = tournamentId),
            View.Categories(tournamentId = tournamentId),
            View.Schedule(tournamentId = tournamentId)
        ),
        onClick = navigateTo,
        onLinkClick = {
            navigateTo(
                View.AddOrUpdateTournament(
                    tournamentId = tournamentId,
                    tournamentName = tournamentName
                )
            )
        }
    )
}