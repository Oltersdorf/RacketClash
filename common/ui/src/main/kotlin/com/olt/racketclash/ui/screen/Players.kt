package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.Database
import com.olt.racketclash.players.Player
import com.olt.racketclash.players.PlayersModel
import com.olt.racketclash.players.Tag
import com.olt.racketclash.state.SortDirection
import com.olt.racketclash.ui.component.*
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll
import com.olt.racketclash.ui.navigate.Screens
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.generated.resources.Res
import racketclash.common.ui.generated.resources.medal

@Composable
internal fun Players(
    database: Database,
    navigateTo: (Screens) -> Unit
) {
    val model = remember { PlayersModel(database = database) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Players",
        onTitleAdd = { navigateTo(Screens.AddOrUpdatePlayer(playerName = null, playerId = null)) },
        items = state.players,
        isLoading = state.isLoading,
        columns = columns(
            navigateTo = navigateTo,
            onNameSort = model::onNameSort,
            onBirthYearSort = model::onBirthYearSort,
            onClubSort = model::onClubSort,
            onTournamentsSort = model::onTournamentSort,
            onMedalsSort = model::onMedalsSort,
            onSinglesSort = model::onSinglesSort,
            onDoublesSort = model::onDoublesSort
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
    onNameSort: (SortDirection) -> Unit,
    onBirthYearSort: (SortDirection) -> Unit,
    onClubSort: (SortDirection) -> Unit,
    onTournamentsSort: (SortDirection) -> Unit,
    onMedalsSort: (SortDirection) -> Unit,
    onSinglesSort: (SortDirection) -> Unit,
    onDoublesSort: (SortDirection) -> Unit,
): List<LazyTableColumn<Player>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.3f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onNameSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onNameSort(SortDirection.Descending)
            }
        }) { navigateTo(Screens.Player(playerName = it.name, playerId = it.id)) },
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onBirthYearSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onBirthYearSort(SortDirection.Descending)
            }
        }) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.3f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onClubSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onClubSort(SortDirection.Descending)
            }
        }) { it.club },
        LazyTableColumn.Text(name = "# tournaments", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onTournamentsSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onTournamentsSort(SortDirection.Descending)
            }
        }) { it.numberOfTournaments.toString() },
        LazyTableColumn.Builder(name = "Medals", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onMedalsSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onMedalsSort(SortDirection.Descending)
            }
        }) { player, weight ->
            Row(modifier = Modifier.weight(weight)) {
                if (player.goldMedals > 0) {
                    Text(text = player.goldMedals.toString())
                    Icon(
                        painter = painterResource(Res.drawable.medal),
                        contentDescription = "Gold",
                        tint = AdditionalMaterialTheme.current.gold
                    )
                }
                if (player.silverMedals > 0) {
                    Text(
                        text = player.silverMedals.toString(),
                        modifier = Modifier.padding(start = if (player.goldMedals > 0) 10.dp else 0.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.medal),
                        contentDescription = "Silver",
                        tint = AdditionalMaterialTheme.current.silver
                    )
                }
                if (player.bronzeMedals > 0) {
                    Text(
                        text = player.bronzeMedals.toString(),
                        modifier = Modifier.padding(start = if (player.goldMedals > 0 || player.silverMedals > 0) 10.dp else 0.dp)
                    )
                    Icon(
                        painter = painterResource(Res.drawable.medal),
                        contentDescription = "Bronze",
                        tint = AdditionalMaterialTheme.current.bronze
                    )
                }
            }
        },
        LazyTableColumn.Builder(name = "Single", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSinglesSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onSinglesSort(SortDirection.Descending)
            }
        }) { player, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioSingle.first,
                middle = player.winRatioSingle.second,
                right = player.winRatioSingle.third
            )
        },
        LazyTableColumn.Builder(name = "Double", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onDoublesSort(SortDirection.Ascending)
                LazyTableSortDirection.Descending -> onDoublesSort(SortDirection.Descending)
            }
        }) { player, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioDouble.first,
                middle = player.winRatioDouble.second,
                right = player.winRatioDouble.third
            )
        }
    )

@Composable
private fun TagText(tagType: Tag) =
    when (tagType) {
        is Tag.BirthYear -> Tag(name = "Birth year", text = tagType.text)
        is Tag.Club -> Tag(name = "Club", text = tagType.text)
        Tag.HasMedals -> Tag(name = "Has medals")
        Tag.HasNoMedals -> Tag(name = "Has no medals")
        is Tag.Name -> Tag(name = "Name", text = tagType.text)
    }