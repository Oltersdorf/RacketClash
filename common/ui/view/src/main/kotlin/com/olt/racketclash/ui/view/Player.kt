package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.Player
import com.olt.racketclash.database.api.PlayerFilter
import com.olt.racketclash.database.api.PlayerSorting
import com.olt.racketclash.state.datetime.toFormattedString
import com.olt.racketclash.state.player.PlayerModel
import com.olt.racketclash.state.player.PlayerTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.*
import com.olt.racketclash.ui.base.material.FilterChip
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.TableSortDirection
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.view.generated.resources.Res
import racketclash.common.ui.view.generated.resources.medal
import kotlin.math.max
import kotlin.math.min

@Composable
internal fun Player(
    database: Database,
    playerId: Long,
    navigateTo: (View) -> Unit
) {
    val model = remember {
        PlayerModel(
            playerDatabase = database.players,
            tournamentDatabase = database.tournaments,
            categoryDatabase = database.categories,
            gameDatabase = database.games,
            playerId = playerId
        )
    }
    val state by model.state.collectAsState()

    RacketClashDetailScaffold(
        title = "Player",
        model = model,
        headerContent = {
            DetailSectionRow(title = state.item.name) {
                DetailText(title = "Birth year", text = state.item.birthYear.toString())
                DetailText(title = "Club", text = state.item.club)
            }

            DetailSectionColumn(title = "Statistics") {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    DetailText(title = "First game", text = state.item.firstGameDate?.toFormattedString() ?: "N/A")
                    DetailText(title = "Last game", text = state.item.lastGameDate?.toFormattedString() ?: "N/A")
                }
            }
        },
        editOverlayContent = {
            AddOrUpdatePlayerOverlay(
                state = state.updatedItem,
                update = model::setUpdatedItem,
                clubSuggestions = state.data.clubSuggestions,
                onGetClubSuggestions = model::clubSuggestions
            )
        }
    ) {
        TournamentPreview(
            isLoading = state.isLoading,
            tournaments = state.data.tournaments,
            navigateTo = navigateTo
        )

        CategoryPreview(
            isLoading = state.isLoading,
            categories = state.data.categories,
            navigateTo = navigateTo
        )
    }
}

@Composable
internal fun Players(
    database: Database,
    navigateTo: (View) -> Unit
) {
    val model = remember { PlayerTableModel(database = database.players) }
    val state by model.state.collectAsState()
    val clubSuggestions by model.clubSuggestionsState.collectAsState()

    RacketClashTableScaffold(
        title = "Players",
        model = model,
        filterOverlayContent = {
            FilterPlayerOverlay(
                state = state.filterUpdate,
                update = model::setFilter
            )
        },
        addOverlayContent = {
            AddOrUpdatePlayerOverlay(
                state = state.addItem,
                update = model::setNewItem,
                clubSuggestions = clubSuggestions,
                onGetClubSuggestions = model::clubSuggestions
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
private fun FilterPlayerOverlay(
    state: PlayerFilter,
    update: (PlayerFilter) -> Unit
) {
    FormTextField(value = state.name, label = "Name") { update(state.copy(name = it)) }

    FormRow {
        FormNumberSelector(
            value = state.birthYear.first,
            label = "Min birth year",
            range = state.birthYear,
            onUp = { update(state.copy(birthYear = it..max(it, state.birthYear.last))) },
            onDown = { update(state.copy(birthYear = it..state.birthYear.last)) }
        )

        FormNumberSelector(
            value = state.birthYear.last,
            label = "Max birth year",
            range = state.birthYear,
            onUp = { update(state.copy(birthYear = state.birthYear.first..it)) },
            onDown = { update(state.copy(birthYear = min(it, state.birthYear.first)..it)) }
        )
    }

    FormTextField(value = state.club, label = "Club") { update(state.copy(club = it)) }

    FormRow {
        FormNumberSelector(
            value = state.medals.first,
            label = "Min medals",
            range = state.medals,
            onUp = { update(state.copy(medals = it..max(it, state.medals.last))) },
            onDown = { update(state.copy(medals = it..state.medals.last)) }
        )

        FormNumberSelector(
            value = state.medals.last,
            label = "Max medals",
            range = state.medals,
            onUp = { update(state.copy(medals = state.medals.first..it)) },
            onDown = { update(state.copy(medals = min(it, state.medals.first)..it)) }
        )
    }
}

@Composable
private fun AddOrUpdatePlayerOverlay(
    state: Player,
    update: (Player) -> Unit,
    clubSuggestions: List<String>,
    onGetClubSuggestions: (String) -> Unit
) {
    FormTextField(
        value = state.name,
        label = "Name",
        isError = state.name.isBlank(),
        onValueChange = { update(state.copy(name = it)) }
    )

    FormDropDownTextField(
        text = state.birthYear.toString(),
        label = "Birth year",
        readOnly = true,
        dropDownItems = (1900..2050).toList(),
        dropDownItemText = { Text(it.toString()) },
        onItemClicked = { update(state.copy(birthYear = it)) }
    )

    FormDropDownTextField(
        text = state.club,
        label = "Club",
        onTextChange = {
            update(state.copy(club = it))
            onGetClubSuggestions(it)
        },
        dropDownItems = clubSuggestions,
        dropDownItemText = { Text(it) },
        onItemClicked = { update(state.copy(club = it)) }
    )
}

private fun columns(
    navigateTo: (View) -> Unit,
    onSort: (PlayerSorting) -> Unit,
    onDelete: (Player) -> Unit
): List<LazyTableColumn<Player>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.25f, text = { it.name }, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(PlayerSorting.NameAsc)
                TableSortDirection.Descending -> onSort(PlayerSorting.NameDesc)
            }
        }) { navigateTo(View.Player(playerId = it.id)) },
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(PlayerSorting.BirthYearAsc)
                TableSortDirection.Descending -> onSort(PlayerSorting.BirthYearDesc)
            }
        }) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.25f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(PlayerSorting.ClubAsc)
                TableSortDirection.Descending -> onSort(PlayerSorting.ClubDesc)
            }
        }) { it.club },
        LazyTableColumn.Text(name = "# tournaments", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(PlayerSorting.TournamentsAsc)
                TableSortDirection.Descending -> onSort(PlayerSorting.TournamentsDesc)
            }
        }) { it.numberOfTournaments.toString() },
        LazyTableColumn.Builder(name = "Medals", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(PlayerSorting.MedalsAsc)
                TableSortDirection.Descending -> onSort(PlayerSorting.MedalsDesc)
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
                TableSortDirection.Ascending -> onSort(PlayerSorting.SinglesAsc)
                TableSortDirection.Descending -> onSort(PlayerSorting.SinglesDesc)
            }
        }) { player, weight ->
            /*RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioSingle.first,
                middle = player.winRatioSingle.second,
                right = player.winRatioSingle.third
            )*/
        },
        LazyTableColumn.Builder(name = "Double", weight = 0.1f, onSort = {
            when (it) {
                TableSortDirection.Ascending -> onSort(PlayerSorting.DoublesAsc)
                TableSortDirection.Descending -> onSort(PlayerSorting.DoublesDesc)
            }
        }) { player, weight ->
            /*RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = player.winRatioDouble.first,
                middle = player.winRatioDouble.second,
                right = player.winRatioDouble.third
            )*/
        },
        LazyTableColumn.IconButton(
            name = "Delete",
            weight = 0.1f,
            enabled = { it.gamesPlayed + it.gamesScheduled == 0L },
            onClick = onDelete,
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete"
        )
    )

@Composable
internal fun PlayerPreview(
    isLoading: Boolean,
    players: List<Player>,
    navigateTo: (View) -> Unit
) {
    ListPreviewBox(
        name = "Players",
        isLoading = isLoading,
        items = players,
        onNavigateMore = { navigateTo(View.Players) }
    ) {
        ListPreviewBoxLink(
            text = it.name,
            subText = "(${it.birthYear})"
        ) { navigateTo(View.Player(playerId = it.id)) }
    }
}