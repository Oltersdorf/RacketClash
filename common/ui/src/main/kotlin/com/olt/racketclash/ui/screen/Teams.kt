package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.RatioBar
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.Tag
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens

private data class Team(
    val id: Long = 0L,
    val name: String = "Test Team",
    val size: Int = 24,
    val winRatioSingle: Triple<Int, Int, Int> = Triple(10, 2, 15),
    val winRatioDouble: Triple<Int, Int, Int> = Triple(14, 0, 16)
)

private sealed class TagTypeTeam {
    data class Name(val text: String) : TagTypeTeam()
}

@Composable
internal fun Teams(
    database: Database,
    tournamentId: Long,
    navigateTo: (Screens) -> Unit
) {
    var searchBarText by remember { mutableStateOf("1") }
    var availableTags by remember { mutableStateOf( listOf(
        TagTypeTeam.Name("1")
    )) }
    var tags by remember { mutableStateOf(listOf(
        TagTypeTeam.Name("1")
    )) }
    var teams by remember { mutableStateOf(listOf(
        Team(),
        Team(winRatioSingle = Triple(1, 0, 100)),
        Team(winRatioSingle = Triple(0, 0, 5)),
        Team(winRatioSingle = Triple(10, 10, 23)),
        Team(winRatioSingle = Triple(10, 10, 100))
    )) }
    var currentPage by remember { mutableStateOf(1) }
    var lastPage by remember { mutableStateOf(2) }
    var isLoading by remember { mutableStateOf(false) }

    SearchableLazyTableWithScroll(
        title = "Teams",
        onTitleAdd = { navigateTo(Screens.AddOrUpdateTeam(teamId = null, teamName = null, tournamentId = tournamentId)) },
        items = teams,
        isLoading = isLoading,
        columns = columns(
            navigateTo = navigateTo,
            tournamentId = tournamentId,
            onNameSortAscending = {},
            onNameSortDescending = {},
            onSizeSortAscending = {},
            onSizeSortDescending = {},
            onSingleSortAscending = {},
            onSingleSortDescending = {},
            onDoubleSortAscending = {},
            onDoubleSortDescending = {}
        ),
        currentPage = currentPage,
        lastPage = lastPage,
        onPageClicked = { currentPage = it }
    ) {
        SearchBar(
            text = searchBarText,
            onTextChange = { searchBarText = it },
            dropDownItems = availableTags,
            onDropDownItemClick = { tags += it },
            tags = tags,
            onTagRemove = { tags -= it },
            tagText = { TagText(it) }
        )
    }
}

private fun columns(
    navigateTo: (Screens) -> Unit,
    tournamentId: Long,
    onNameSortAscending: () -> Unit,
    onNameSortDescending: () -> Unit,
    onSizeSortAscending: () -> Unit,
    onSizeSortDescending: () -> Unit,
    onSingleSortAscending: () -> Unit,
    onSingleSortDescending: () -> Unit,
    onDoubleSortAscending: () -> Unit,
    onDoubleSortDescending: () -> Unit
): List<LazyTableColumn<Team>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.8f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSortAscending()
                LazyTableSortDirection.Descending -> onNameSortDescending()
            }
        }) {
            navigateTo(Screens.Team(teamName = it.name, teamId = it.id, tournamentId = tournamentId))
        },
        LazyTableColumn.Text(name = "Size", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSizeSortAscending()
                LazyTableSortDirection.Descending -> onSizeSortDescending()
            }
        }) { it.size.toString() },
        LazyTableColumn.Builder(name = "Single", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSingleSortAscending()
                LazyTableSortDirection.Descending -> onSingleSortDescending()
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
                LazyTableSortDirection.Ascending -> onDoubleSortAscending()
                LazyTableSortDirection.Descending -> onDoubleSortDescending()
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
private fun TagText(tagType: TagTypeTeam) =
    when (tagType) {
        is TagTypeTeam.Name -> Tag(name = "Name", text = tagType.text)
    }