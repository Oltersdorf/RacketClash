package com.olt.racketclash.ui.view

import androidx.compose.runtime.Composable
import com.olt.racketclash.database.api.Category
import com.olt.racketclash.database.api.Schedule
import com.olt.racketclash.database.api.Team
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.ListPreviewBox
import com.olt.racketclash.ui.base.layout.ListPreviewBoxLink
import com.olt.racketclash.ui.layout.RacketClashScrollableScaffold

@Composable
internal fun Tournament(
    tournamentId: Long,
    tournamentName: String,
    navigateTo: (View) -> Unit
) {
    RacketClashScrollableScaffold(
        title = "Tournament: $tournamentName",
        headerContent = {}
    ) {
        ListPreviewBox(
            name = "Teams",
            isLoading = false,
            items = emptyList<Team>(),
            onNavigateMore = { navigateTo(View.Teams(tournamentId = tournamentId)) }
        ) {
            ListPreviewBoxLink(
                text = it.name,
                subText = "(Rank: ${it.rank}, Size: ${it.size})"
            ) { navigateTo(View.Team(teamName = it.name, teamId = it.id, tournamentId = it.tournamentId)) }
        }

        ListPreviewBox(
            name = "Categories",
            isLoading = false,
            items = emptyList<Category>(),
            onNavigateMore = { navigateTo(View.Categories(tournamentId = tournamentId)) }
        ) {
            ListPreviewBoxLink(
                text = it.name,
                subText = "(Type: ${it.type})"
            ) { navigateTo(View.Category(categoryName = it.name, categoryId = it.id, tournamentId = it.tournamentId)) }
        }

        ListPreviewBox(
            name = "Schedule",
            isLoading = false,
            items = emptyList<Schedule>(),
            onNavigateMore = { navigateTo(View.Schedule(tournamentId = tournamentId)) }
        ) {
            ListPreviewBoxLink(
                text = "Schedule",
                subText = ""
            ) { navigateTo(View.Schedule(tournamentId = it.tournamentId)) }
        }
    }
}