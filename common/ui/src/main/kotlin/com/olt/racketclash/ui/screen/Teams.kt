package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.database.team.DeletableTeam
import com.olt.racketclash.database.team.Sorting
import com.olt.racketclash.teams.TeamsModel
import com.olt.racketclash.ui.component.*
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Teams(
    database: Database,
    tournamentId: Long,
    navigateTo: (Screens) -> Unit
) {
    val model = remember { TeamsModel(database = database, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Teams",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateTeam(teamId = null, teamName = null, tournamentId = tournamentId)) },
        items = state.teams,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
            tournamentId = tournamentId,
            onSort = model::onSort
        ),
        currentPage = state.currentPage,
        lastPage = state.lastPage,
        onPageClicked = model::updatePage
    ) {
        SearchBar(
            text = state.searchBarText,
            onTextChange = model::updateSearchBar,
            dropDownItems = {
                state.availableTags.name?.let {
                    SearchBarMenuItem(name = "Name", text = it, onClick = model::addNameTag)
                }
            }
        ) {
            state.tags.name?.let {
                SearchBarTagChip(name = "Name", text = it, onRemove = model::removeNameTag)
            }
        }
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    tournamentId: Long,
    onSort: (Sorting) -> Unit
): List<LazyTableColumn<DeletableTeam>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.NameDesc)
            }
        }) {
            navigateTo(Screens.Team(teamName = it.name, teamId = it.id, tournamentId = tournamentId))
        },
        LazyTableColumn.Text(name = "Size", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.SizeAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.SizeDesc)
            }
        }) { it.size.toString() },
        LazyTableColumn.Builder(name = "Single", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.SinglesAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.SinglesDesc)
            }
        }) { team, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = team.winRatioSingle.first,
                middle = team.winRatioSingle.second,
                right = team.winRatioSingle.third
            )
        },
        LazyTableColumn.Builder(name = "Double", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.DoublesAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.DoublesDesc)
            }
        }) { team, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = team.winRatioDouble.first,
                middle = team.winRatioDouble.second,
                right = team.winRatioDouble.third
            )
        }
    )