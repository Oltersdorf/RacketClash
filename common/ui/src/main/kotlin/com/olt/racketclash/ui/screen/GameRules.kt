package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.Link
import com.olt.racketclash.ui.component.SimpleIconButton
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

private data class GameRule(
    val id: Long = 0L,
    val name: String = "Test",
    val maxSets: Int = 3,
    val winSets: Int = 2,
    val maxPoints: Int = 30,
    val winPoints: Int = 21,
    val pointsDifference: Int = 2,
    val gamePointsForWin: Int = 2,
    val gamePointsForLose: Int = 0,
    val gamePointsForDraw: Int = 1,
    val gamePointsForRest: Int = 2,
    val setPointsForRest: Int = 0,
    val pointPointsForRest: Int = 0
)

@Composable
internal fun GameRules(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    //TODD: replace with database
    var gameRules by remember { mutableStateOf(listOf(GameRule())) }

    LazyTableWithScroll(
        header = {
            Text(
                text = "Game rules",
                modifier = Modifier.weight(1.0f).padding(start = 10.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge
            )
            SimpleIconButton(
                modifier = Modifier.padding(top = 5.dp, end = 5.dp, bottom = 5.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            ) { navigateTo(Screens.AddOrUpdateGameRule(gameRuleName = null, gameRuleId = null)) }
        },
        items = gameRules,
        showHeader = false,
        columns = listOf(
            LazyTableColumn.Builder { gameRule, weight ->
                Column(modifier = Modifier.weight(weight)) {
                    Link(text = gameRule.name, style = MaterialTheme.typography.titleMedium) {
                        navigateTo(Screens.AddOrUpdateGameRule(gameRuleName = gameRule.name, gameRuleId = gameRule.id))
                    }

                    Text( text =
                        "${gameRule.winSets}/${gameRule.maxSets} sets, " +
                        "${gameRule.winPoints}/${gameRule.maxPoints} +/- ${gameRule.pointsDifference} points"
                    )
                    Text( text =
                        "Rating: W:${gameRule.gamePointsForWin} / D:${gameRule.gamePointsForDraw} / L:${gameRule.gamePointsForLose}, " +
                        "Rest: G:${gameRule.gamePointsForRest} / S:${gameRule.setPointsForRest} / P:${gameRule.pointPointsForRest}"
                    )
                }
            }
        )
    )
}