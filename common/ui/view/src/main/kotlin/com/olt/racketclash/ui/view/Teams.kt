package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamFilter
import com.olt.racketclash.database.api.TeamSorting
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.teams.TeamsModel
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableSortDirection
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.material.RatioBar
import com.olt.racketclash.ui.layout.FilteredLazyTable

@Composable
internal fun Teams(
    database: Database,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { TeamsModel(database = database.teams, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    FilteredLazyTable(
        state = ListState(
            isLoading = state.isLoading,
            items = state.teams,
            filter = TeamFilter(),
            sorting = TeamSorting.NameAsc
        ),
        columns = columns(
            navigateTo = navigateTo,
            tournamentId = tournamentId,
            onSort = model::onSort,
            onDelete = model::onDelete
        ),
        onPageClicked = model::updatePage
    ) {

    }
}

private fun columns(
    navigateTo: (View) -> Unit,
    tournamentId: Long,
    onSort: (TeamSorting) -> Unit,
    onDelete: (Team) -> Unit
): List<LazyTableColumn<Team>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TeamSorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(TeamSorting.NameDesc)
            }
        }) {
            navigateTo(View.Team(teamName = it.name, teamId = it.id, tournamentId = tournamentId))
        },
        LazyTableColumn.Text(name = "Rank", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TeamSorting.RankAsc)
                LazyTableSortDirection.Descending -> onSort(TeamSorting.RankDesc)
            }
        }) { it.rank.toString() },
        LazyTableColumn.Text(name = "Size", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TeamSorting.SizeAsc)
                LazyTableSortDirection.Descending -> onSort(TeamSorting.SizeDesc)
            }
        }) { it.size.toString() },
        LazyTableColumn.Builder(name = "Games", weight = 0.1f) { team, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = team.gamesWon.toInt(),
                middle = team.gamesDraw.toInt(),
                right = team.gamesLost.toInt()
            )
        },
        LazyTableColumn.Builder(name = "Points", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(TeamSorting.PointsAsc)
                LazyTableSortDirection.Descending -> onSort(TeamSorting.PointsDesc)
            }
        }) { team, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = team.gamePointsWon.toInt(),
                right = team.gamePointsLost.toInt()
            )
        },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = onDelete,
            enabled = { it.size == 0L },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )