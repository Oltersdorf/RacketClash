package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamFilter
import com.olt.racketclash.database.api.TeamSorting
import com.olt.racketclash.state.team.TeamModel
import com.olt.racketclash.state.team.TeamTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.*
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.material.RatioBar

@Composable
internal fun Team(
    database: Database,
    teamId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember {
        TeamModel(
            teamDatabase = database.teams,
            playerDatabase = database.players,
            teamId = teamId
        )
    }
    val state by model.state.collectAsState()

    RacketClashDetailScaffold(
        title = "Team",
        model = model,
        headerContent = {
            DetailSectionRow(title = state.item.name) {
                DetailText(title = "id", text = state.item.id.toString())
                DetailText(title = "Size", text = state.item.size.toString())
            }
        },
        editOverlayContent = {
            AddOrUpdateTeamOverlay(
                state = state.updatedItem,
                update = model::setUpdatedItem
            )
        }
    ) {
        PlayerPreview(
            isLoading = state.isLoading,
            players = state.data.players,
            navigateTo = navigateTo
        )
    }
}

@Composable
internal fun Teams(
    database: Database,
    tournamentId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember { TeamTableModel(database = database.teams, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    RacketClashTableScaffold(
        title = "Teams",
        model = model,
        filterOverlayContent = {
            FilterTeamOverlay(
                state = state.filterUpdate,
                update = model::setFilter
            )
        },
        addOverlayContent = {
            AddOrUpdateTeamOverlay(
                state = state.addItem,
                update = model::setNewItem
            )
        }
    ) {
        FilteredTable(
            state = state,
            columns = columns(
                navigateTo = navigateTo,
                onSort = model::sort,
                onDelete = model::delete
            ),
            onPageClicked = model::selectPage
        ) {
            if (it.name.isNotBlank())
                FilterChip(name = "Name", text = it.name) { model.setAndApplyFilter(it.copy(name = "")) }
        }
    }
}

@Composable
private fun FilterTeamOverlay(
    state: TeamFilter,
    update: (TeamFilter) -> Unit
) {
    FormTextField(value = state.name, label = "Name") { update(state.copy(name = it)) }
}

@Composable
private fun AddOrUpdateTeamOverlay(
    state: Team,
    update: (Team) -> Unit
) {
    FormRow {
        FormTextField(
            value = state.name,
            label = "Name",
            isError = state.name.isBlank(),
            onValueChange = { update(state.copy(name = it)) }
        )

        FormNumberSelector(
            value = state.rank,
            label = "Rank",
            range = 1..Int.MAX_VALUE,
            onUp = { update(state.copy(rank = it)) },
            onDown = { update(state.copy(rank = it)) }
        )
    }
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (TeamSorting) -> Unit,
    onDelete: (Team) -> Unit
): List<LazyTableColumn<Team>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TeamSorting.NameAsc)
                TableSortDirection.Descending -> onSort(TeamSorting.NameDesc)
            }
        }) {
            navigateTo(View.Team(teamId = it.id))
        },
        LazyTableColumn.Text(name = "Rank", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TeamSorting.RankAsc)
                TableSortDirection.Descending -> onSort(TeamSorting.RankDesc)
            }
        }) { it.rank.toString() },
        LazyTableColumn.Text(name = "Size", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(TeamSorting.SizeAsc)
                TableSortDirection.Descending -> onSort(TeamSorting.SizeDesc)
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
                TableSortDirection.Ascending -> onSort(TeamSorting.PointsAsc)
                TableSortDirection.Descending -> onSort(TeamSorting.PointsDesc)
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

@Composable
internal fun TeamPreview(
    isLoading: Boolean,
    teams: List<Team>,
    navigateTo: (View) -> Unit
) {
    ListPreviewBox(
        name = "Teams",
        isLoading = isLoading,
        items = teams,
        onNavigateMore = { navigateTo(View.Teams(tournamentId = 0L)) }
    ) {
        ListPreviewBoxLink(
            text = it.name,
            subText = "(Rank: ${it.rank}, Size: ${it.size})"
        ) { navigateTo(View.Team(teamId = it.id)) }
    }
}