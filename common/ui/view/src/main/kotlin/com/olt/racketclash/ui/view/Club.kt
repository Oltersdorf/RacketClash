package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.olt.racketclash.database.api.Club
import com.olt.racketclash.database.api.ClubFilter
import com.olt.racketclash.database.api.ClubSorting
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.state.club.ClubModel
import com.olt.racketclash.state.club.ClubTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.FormTextField
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
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

    RacketClashDetailScaffold(
        title = "Club",
        model = model,
        headerContent = {
            DetailSectionRow(title = state.item.name) {
                Column {
                    DetailText(title = "id", text = state.item.id.toString())
                    DetailText(title = "players", text = state.item.players.toString())
                }
            }
        },
        editOverlayContent = {
            AddOrUpdateClubOverlay(
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

        TournamentPreview(
            isLoading = state.isLoading,
            tournaments = state.data.tournaments,
            navigateTo = navigateTo
        )
    }
}

@Composable
internal fun Clubs(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { ClubTableModel(database = database.clubs) }
    val state by model.state.collectAsState()

    RacketClashTableScaffold(
        title = "Clubs",
        model = model,
        filterOverlayContent = {
            FilterClubOverlay(
                state = state.filterUpdate,
                update = model::setFilter
            )
        },
        addOverlayContent = {
            AddOrUpdateClubOverlay(
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
private fun FilterClubOverlay(
    state: ClubFilter,
    update: (ClubFilter) -> Unit
) {
    FormTextField(value = state.name, label = "Name") { update(state.copy(name = it)) }
}

@Composable
private fun AddOrUpdateClubOverlay(
    state: Club,
    update: (Club) -> Unit
) {
    FormTextField(
        value = state.name,
        label = "Name",
        isError = state.name.isBlank(),
        onValueChange = { update(state.copy(name = it)) }
    )
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