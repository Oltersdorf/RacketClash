package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.teams.Tag
import com.olt.racketclash.teams.Team
import com.olt.racketclash.teams.TeamsModel
import com.olt.racketclash.ui.component.RatioBar
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

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
            onNameSort = model::onNameSort,
            onSizeSort = model::onSizeSort,
            onSingleSort = model::onSingleSort,
            onDoubleSort = model::onDoubleSort
        ),
        currentPage = state.currentPage,
        lastPage = state.lastPage,
        onPageClicked = model::updatePage
    ) {
        SearchBar(
            text = state.searchBarText,
            onTextChange = model::updateSearchBar,
            dropDownItems = state.availableTags,
            onDropDownItemClick = model::addTag,
            tags = state.tags,
            onTagRemove = model::removeTag,
            tagText = { TagText(it) }
        )
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    tournamentId: Long,
    onNameSort: (SortDirection) -> Unit,
    onSizeSort: (SortDirection) -> Unit,
    onSingleSort: (SortDirection) -> Unit,
    onDoubleSort: (SortDirection) -> Unit,
): List<LazyTableColumn<Team>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onNameSort(SortDirection.Descending)
            }
        }) {
            navigateTo(Screens.Team(teamName = it.name, teamId = it.id, tournamentId = tournamentId))
        },
        LazyTableColumn.Text(name = "Size", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSizeSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onSizeSort(SortDirection.Descending)
            }
        }) { it.size.toString() },
        LazyTableColumn.Builder(name = "Single", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSingleSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onSingleSort(SortDirection.Descending)
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
                LazyTableSortDirection.Ascending -> onDoubleSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onDoubleSort(SortDirection.Descending)
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

@Composable
private fun TagText(tagType: Tag) =
    when (tagType) {
        is Tag.Name -> Tag(name = "Name", text = tagType.text)
    }