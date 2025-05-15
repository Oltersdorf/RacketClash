package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Team
import com.olt.racketclash.database.api.TeamFilter
import com.olt.racketclash.database.api.TeamSorting
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.state.team.TeamModel
import com.olt.racketclash.state.team.TeamState
import com.olt.racketclash.state.team.TeamTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.*
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.SimpleIconButton
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
    var showEditOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Team",
        headerContent = { TeamInfo(isLoading = state.isLoading, team = state.team) },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) { showEditOverlay = !showEditOverlay }
        },
        overlay = {
            AddOrUpdateTeamOverlay(
                visible = showEditOverlay,
                team = state.team,
                onConfirm = model::updateTeam
            ) { showEditOverlay = false }
        }
    ) {
        TeamBody(
            state = state,
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
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Teams",
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Search,
                contentDescription = "Filter"
            ) {
                showAddOverlay = false
                showFilterOverlay = !showFilterOverlay
            }

            SimpleIconButton(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            ) {
                showFilterOverlay = false
                showAddOverlay = !showAddOverlay
            }
        },
        overlay = {
            FilterTeamOverlay(
                visible = showFilterOverlay,
                filter = state.filter,
                applyFilter = model::filter
            ) { showFilterOverlay = false }

            AddOrUpdateTeamOverlay(
                visible = showAddOverlay,
                onConfirm = model::add
            ) { showAddOverlay = false }
        }
    ) {
        TeamTable(
            state = state,
            onSort = model::sort,
            onDelete = model::delete,
            onSelectPage = model::selectPage,
            onNavigateTo = navigateTo,
            onApplyFilter = model::filter
        )
    }
}

@Composable
private fun BoxScope.FilterTeamOverlay(
    filter: TeamFilter,
    applyFilter: (TeamFilter) -> Unit,
    visible: Boolean,
    dismissOverlay: () -> Unit
) {
    FilterFormOverlay(
        filterState = filter,
        visible = visible,
        dismissOverlay = dismissOverlay,
        onFilter = applyFilter
    ) { state, update ->
        FormTextField(value = state.name, label = "Name") { update { copy(name = it) } }
    }
}

@Composable
private fun BoxScope.AddOrUpdateTeamOverlay(
    visible: Boolean,
    team: Team? = null,
    onConfirm: (Team) -> Unit,
    dismissOverlay: () -> Unit
) {
    AddOrUpdateFormOverlay(
        defaultItemState = Team(),
        itemState = team,
        visible = visible,
        dismissOverlay = dismissOverlay,
        canConfirm = { it.name.isNotBlank() },
        onConfirm = onConfirm
    ) { state, update ->
        FormRow {
            FormTextField(
                value = state.name,
                label = "Name",
                isError = state.name.isBlank(),
                onValueChange = { update { copy(name = it) } }
            )

            FormNumberSelector(
                value = state.rank,
                label = "Rank",
                range = 1..Int.MAX_VALUE,
                onUp = { update { copy(rank = it) } },
                onDown = { update { copy(rank = it) } }
            )
        }
    }
}

@Composable
private fun TeamTable(
    state: ListState<Team, TeamFilter, TeamSorting>,
    onSort: (TeamSorting) -> Unit,
    onDelete: (Team) -> Unit,
    onSelectPage: (Int) -> Unit,
    onNavigateTo: (View) -> Unit,
    onApplyFilter: (TeamFilter) -> Unit
) {
    FilteredTable(
        state = state,
        columns = columns(
            navigateTo = onNavigateTo,
            onSort = onSort,
            onDelete = onDelete
        ),
        onPageClicked = onSelectPage
    ) {
        if (it.name.isNotBlank())
            FilterChip(name = "Name", text = it.name) { onApplyFilter(it.copy(name = "")) }
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
private fun TeamInfo(
    isLoading: Boolean,
    team: Team
) {
    Details(
        isLoading = isLoading,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        DetailSectionRow(title = team.name) {
            DetailText(title = "id", text = team.id.toString())
            DetailText(title = "Size", text = team.size.toString())
        }
    }
}

@Composable
private fun TeamBody(
    state: TeamState,
    navigateTo: (View) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
        ListPreviewBox(
            name = "Players",
            isLoading = state.isLoading,
            items = state.players,
            onNavigateMore = { navigateTo(View.Players) }
        ) {
            ListPreviewBoxLink(
                text = it.name,
                subText = "(${it.club})"
            ) { navigateTo(View.Player(playerId = it.id)) }
        }
    }
}