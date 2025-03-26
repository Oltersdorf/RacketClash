package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Rule
import com.olt.racketclash.state.rule.RuleModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.layout.AddOrUpdateRuleOverlay
import com.olt.racketclash.ui.layout.RacketClashScaffold
import com.olt.racketclash.ui.material.SimpleIconButton

private sealed class SelectedList {
    data object Tournaments : SelectedList()
    data object Categories : SelectedList()
    data object Games : SelectedList()
}

@Composable
internal fun Rule(
    database: Database,
    id: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { RuleModel(ruleDatabase = database.rules, id = id) }
    val state by model.state.collectAsState()
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showEditOverlay by remember { mutableStateOf(false) }
    var selectedList by remember { mutableStateOf<SelectedList>(SelectedList.Tournaments) }

    RacketClashScaffold(
        title = "Rule: ${state.rule.name}",
        headerContent = {
            Column {
                Info(rule = state.rule)
            }
        },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Search,
                contentDescription = "Filter"
            ) {
                showEditOverlay = false
                showFilterOverlay = !showFilterOverlay
            }

            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) {
                showFilterOverlay = false
                showEditOverlay = !showEditOverlay
            }
        },
        overlay = {
            AddOrUpdateRuleOverlay(
                visible = showEditOverlay,
                rule = state.rule,
                onConfirm = { model.updateRule(rule = it) }
            ) { showEditOverlay = false }
        }
    ) {
        Body(
            selectedList = selectedList,
            onChangeSelectedList = { selectedList = it },
            navigateTo = navigateTo
        )
    }
}

@Composable
private fun Info(rule: Rule) {
    Row(
        modifier = Modifier.padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column {
            Text("id: ${rule.id}")
            Text("Sets: ${rule.winSets}/${rule.maxSets}")
            Text("Rating: W:${rule.gamePointsForWin} / D:${rule.gamePointsForDraw} / L:${rule.gamePointsForLose}")
        }
        Column {
            Text("Used: ${rule.used}")
            Text("Sets: ${rule.winSets}/${rule.maxSets}")
            Text("Rest: G:${rule.gamePointsForRest} / S:${rule.setPointsForRest} / P:${rule.pointPointsForRest}")
        }
    }
}

@Composable
private fun Body(
    selectedList: SelectedList,
    onChangeSelectedList: (SelectedList) -> Unit,
    navigateTo: (View) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
        TabRow(
            selectedTabIndex = when (selectedList) {
                SelectedList.Tournaments -> 0
                SelectedList.Categories -> 1
                SelectedList.Games -> 2
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Tab(
                selected = selectedList is SelectedList.Tournaments,
                onClick = { onChangeSelectedList(SelectedList.Tournaments) }
            ) {
                Text(
                    text = "Tournaments",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Tab(
                selected = selectedList is SelectedList.Categories,
                onClick = { onChangeSelectedList(SelectedList.Categories) }
            ) {
                Text(
                    text = "Categories",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Tab(
                selected = selectedList is SelectedList.Games,
                onClick = { onChangeSelectedList(SelectedList.Games) }
            ) {
                Text(
                    text = "Games",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        when (selectedList) {
            SelectedList.Tournaments -> TournamentTable(navigateTo = navigateTo)
            SelectedList.Categories -> CategoryTable(navigateTo = navigateTo)
            SelectedList.Games -> GameTable(navigateTo = navigateTo)
        }
    }
}

@Composable
private fun TournamentTable(
    navigateTo: (View) -> Unit
) {

}

@Composable
private fun CategoryTable(
    navigateTo: (View) -> Unit
) {

}

@Composable
private fun GameTable(
    navigateTo: (View) -> Unit
) {

}