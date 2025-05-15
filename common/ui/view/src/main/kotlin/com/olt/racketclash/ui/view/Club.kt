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
import com.olt.racketclash.database.api.Club
import com.olt.racketclash.database.api.ClubFilter
import com.olt.racketclash.database.api.ClubSorting
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.state.club.ClubModel
import com.olt.racketclash.state.club.ClubTableModel
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.AddOrUpdateFormOverlay
import com.olt.racketclash.ui.base.layout.FilterFormOverlay
import com.olt.racketclash.ui.base.layout.FormTextField
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.SimpleIconButton
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.*

@Composable
internal fun Club(
    database: Database,
    clubId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember {
        ClubModel(
            clubDatabase = database.clubs,
            playerDatabase = database.players,
            tournamentDatabase = database.tournaments,
            clubId = clubId
        )
    }
    val state by model.state.collectAsState()
    var showEditOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Club",
        headerContent = { ClubInfo(isLoading = state.isLoading, club = state.club) },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) { showEditOverlay = !showEditOverlay }
        },
        overlay = {
            AddOrUpdateClubOverlay(
                visible = showEditOverlay,
                club = state.club,
                onConfirm = model::updateClub
            ) { showEditOverlay = false }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
            PlayerPreview(
                isLoading = state.isLoading,
                players = state.players,
                navigateTo = navigateTo
            )

            TournamentPreview(
                isLoading = state.isLoading,
                tournaments = state.tournaments,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
internal fun Clubs(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { ClubTableModel(database = database.clubs) }
    val state by model.state.collectAsState()
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Clubs",
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
            FilterClubOverlay(
                filter = state.filter,
                applyFilter = model::filter,
                visible = showFilterOverlay
            ) { showFilterOverlay = false }

            AddOrUpdateClubOverlay(
                visible = showAddOverlay,
                onConfirm = model::add
            ) { showAddOverlay = false }
        }
    ) {
        ClubTable(
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
private fun BoxScope.FilterClubOverlay(
    filter: ClubFilter,
    applyFilter: (ClubFilter) -> Unit,
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
private fun BoxScope.AddOrUpdateClubOverlay(
    visible: Boolean,
    club: Club? = null,
    onConfirm: (Club) -> Unit,
    dismissOverlay: () -> Unit
) {
    AddOrUpdateFormOverlay(
        defaultItemState = Club(),
        itemState = club,
        visible = visible,
        dismissOverlay = dismissOverlay,
        canConfirm = { it.name.isNotBlank() },
        onConfirm = onConfirm
    ) { state, update ->
        FormTextField(
            value = state.name,
            label = "Name",
            isError = state.name.isBlank(),
            onValueChange = { update { copy(name = it) } }
        )
    }
}

@Composable
private fun ClubTable(
    state: ListState<Club, ClubFilter, ClubSorting>,
    onSort: (ClubSorting) -> Unit,
    onDelete: (Club) -> Unit,
    onSelectPage: (Int) -> Unit,
    onNavigateTo: (View) -> Unit,
    onApplyFilter: (ClubFilter) -> Unit
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
    onSort: (ClubSorting) -> Unit,
    onDelete: (Club) -> Unit
): List<LazyTableColumn<Club>> =
    listOf(
        LazyTableColumn.Link(name = "Name", text = { it.name }, weight = 0.9f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(ClubSorting.NameAsc)
                TableSortDirection.Descending -> onSort(ClubSorting.NameDesc)
            }
        }) {
            navigateTo(View.Club(clubId = it.id))
        },
        LazyTableColumn.Text(name = "Players", weight = 0.1f) { it.players.toString() },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            onClick = onDelete,
            enabled = { it.players == 0L },
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )

@Composable
private fun ClubInfo(
    isLoading: Boolean,
    club: Club
) {
    Details(
        isLoading = isLoading,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        DetailSectionRow(title = club.name) {
            Column {
                DetailText(title = "id", text = club.id.toString())
                DetailText(title = "players", text = club.players.toString())
            }
        }
    }
}