package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.state.start.StartModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.ListPreviewBox
import com.olt.racketclash.ui.base.layout.ListPreviewBoxLink
import com.olt.racketclash.ui.layout.RacketClashScrollableScaffold
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun Start(database: Database, navigateTo: (View) -> Unit) {
    val model = remember {
        StartModel(
            tournamentDatabase = database.tournaments,
            playerDatabase = database.players,
            ruleDatabase = database.rules
        )
    }
    val state by model.state.collectAsState()

    RacketClashScrollableScaffold(title = "Start") {
        Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
            ListPreviewBox(
                name = "Tournaments",
                isLoading = state.isLoading,
                items = state.tournaments,
                onNavigateMore = { navigateTo(View.Tournaments) }
            ) {
                val startTime = remember {
                    LocalDateTime
                        .ofInstant(it.start, ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm z"))
                }
                val endTime = remember {
                    LocalDateTime
                        .ofInstant(it.end, ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm z"))
                }

                ListPreviewBoxLink(
                    text = it.name,
                    subText = "($startTime to $endTime)"
                ) { navigateTo(View.Tournament(tournamentName = it.name, tournamentId = it.id)) }
            }

            ListPreviewBox(
                name = "Players",
                isLoading = state.isLoading,
                items = state.players,
                onNavigateMore = { navigateTo(View.Players) }
            ) {
                ListPreviewBoxLink(
                    text = it.name,
                    subText = "(${it.club})"
                ) { navigateTo(View.Player(playerName = it.name, playerId = it.id)) }
            }

            ListPreviewBox(
                name = "Rules",
                isLoading = state.isLoading,
                items = state.rules,
                onNavigateMore = { navigateTo(View.Rules) }
            ) {
                ListPreviewBoxLink(
                    text = it.name,
                    subText = "(used: ${it.used}, sets: ${it.winSets}/${it.maxSets}, points: ${it.winPoints}/${it.maxPoints} +/- ${it.pointsDifference}, rating: W:${it.gamePointsForWin} / D:${it.gamePointsForDraw} / L:${it.gamePointsForLose}, rest: G:${it.gamePointsForRest} / S:${it.setPointsForRest} / P:${it.pointPointsForRest})"
                ) { navigateTo(View.Rule(id = it.id)) }
            }
        }
    }
}

