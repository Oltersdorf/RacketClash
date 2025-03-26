package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olt.racketclash.category.CategoryModel
import com.olt.racketclash.category.State
import com.olt.racketclash.database.api.CategoryType
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.ui.base.material.Loading
import com.olt.racketclash.ui.material.Status
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.View

@Composable
internal fun Category(
    database: Database,
    categoryId: Long,
    categoryName: String,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { CategoryModel(database = database.games, categoryId = categoryId) }
    val state by model.state.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        var selectedTab by remember { mutableIntStateOf(0) }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 }
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = "Games",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }
            Tab(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 }
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = "Ranking",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }
        }

        if (state.isLoading)
            Loading()
        else {
            when (selectedTab) {
                0 -> when (state.type) {
                    CategoryType.Custom -> Custom(
                        categoryId = categoryId,
                        categoryName = categoryName,
                        tournamentId = tournamentId,
                        state = state,
                        updatePage = model::updatePage,
                        navigateTo = navigateTo
                    )
                    CategoryType.Table -> Table()
                    CategoryType.Tree -> Tree()
                }
            }
        }
    }
}

@Composable
private fun Custom(
    categoryId: Long,
    categoryName: String,
    tournamentId: Long,
    state: State,
    updatePage: (Int) -> Unit,
    navigateTo: (View) -> Unit
) {
    SearchableLazyTableWithScroll(
        title = "Games",
        onTitleAdd = {
            navigateTo(
                View.AddSchedule(
                categoryId = categoryId,
                categoryName = categoryName,
                tournamentId = tournamentId
            ))
        },
        items = state.games,
        isLoading = false,
        searchBar = {},
        currentPage = state.currentPage,
        lastPage = state.currentPage,
        onPageClicked = updatePage,
        columns = listOf(
            LazyTableColumn.Builder(name = "Status", weight = 0.1f) { game, weight ->
                Status(modifier = Modifier.weight(weight), isOkay = game.submitted != null)
            },
            LazyTableColumn.Text(name = "Scheduled", weight = 0.1f) { it.scheduled.toString() },
            LazyTableColumn.Text(name = "Finished", weight = 0.1f) { it.submitted?.toString() ?: "Pending" },
            LazyTableColumn.Builder(name = "Left", weight = 0.3f) { game, weight ->
                Column(modifier = Modifier.weight(weight)) {
                    Text(
                        text = "${game.playerNameLeftOne} (${game.playerTeamNameLeftOne})",
                        fontWeight = if (game.leftWon == true) FontWeight.Bold else null
                    )
                    if (game.playerNameLeftTwo != null && game.playerTeamNameLeftTwo != null)
                        Text(
                            text = "${game.playerNameLeftTwo} (${game.playerTeamNameLeftOne})",
                            fontWeight = if (game.leftWon == true) FontWeight.Bold else null
                        )
                }
            },
            LazyTableColumn.Builder(name = "Results", weight = 0.1f) { game, weight ->
                Column(modifier = Modifier.weight(weight)) {
                    if (game.sets.isEmpty())
                        Text("Pending")
                    else
                        game.sets.forEach {
                            Row {
                                Text(
                                    text = it.leftPoints.toString(),
                                    fontWeight = if (it.leftPoints > it.rightPoints) FontWeight.Bold else null
                                )
                                Text(" : ")
                                Text(
                                    text = it.rightPoints.toString(),
                                    fontWeight = if (it.rightPoints > it.leftPoints) FontWeight.Bold else null
                                )
                            }
                        }
                }
            },
            LazyTableColumn.Builder(name = "Right", weight = 0.3f) { game, weight ->
                Column(modifier = Modifier.weight(weight)) {
                    Text(
                        text = "${game.playerNameRightOne} (${game.playerTeamNameRightOne})",
                        fontWeight = if (game.leftWon == false) FontWeight.Bold else null
                    )
                    if (game.playerNameRightTwo != null && game.playerTeamNameRightTwo != null)
                        Text(
                            text = "${game.playerNameRightTwo} (${game.playerTeamNameRightTwo})",
                            fontWeight = if (game.leftWon == false) FontWeight.Bold else null
                        )
                }
            },
        )
    )
}

@Composable
private fun Table() {

}

@Composable
private fun Tree() {

}