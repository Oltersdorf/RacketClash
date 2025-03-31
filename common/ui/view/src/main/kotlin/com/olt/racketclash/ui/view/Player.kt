package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.*
import com.olt.racketclash.state.datetime.toFormattedString
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.state.player.PlayerModel
import com.olt.racketclash.state.player.PlayerState
import com.olt.racketclash.state.player.PlayerTableModel
import com.olt.racketclash.ui.View
import com.olt.racketclash.ui.base.layout.*
import com.olt.racketclash.ui.base.material.*
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
            id = playerId
        )
    }
    val state by model.state.collectAsState()
    var showEditOverlay by remember { mutableStateOf(false) }

    RacketClashScrollableScaffold(
        title = "Player",
        headerContent = { PlayerInfo(isLoading = state.isLoading, player = state.player) },
        actions = {
            SimpleIconButton(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            ) { showEditOverlay = !showEditOverlay }
        },
        overlay = {
            AddOrUpdatePlayerOverlay(
                visible = showEditOverlay,
                player = state.player,
                clubSuggestions = state.clubSuggestions,
                onGetClubSuggestions = model::clubSuggestions,
                onConfirm = model::updatePlayer
            ) { showEditOverlay = false }
        }
    ) {
        PlayerBody(
            state = state,
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
    var showFilterOverlay by remember { mutableStateOf(false) }
    var showAddOverlay by remember { mutableStateOf(false) }

    RacketClashScaffold(
        title = "Players",
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
            FilterPlayerOverlay(
                visible = showFilterOverlay,
                filter = state.filter,
                applyFilter = model::filter
            ) { showFilterOverlay = false }

            AddOrUpdatePlayerOverlay(
                visible = showAddOverlay,
                clubSuggestions = clubSuggestions,
                onGetClubSuggestions = model::clubSuggestions,
                onConfirm = model::add
            ) { showAddOverlay = false }
        }
    ) {
        PlayerTable(
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
internal fun BoxScope.FilterPlayerOverlay(
    filter: PlayerFilter,
    applyFilter: (PlayerFilter) -> Unit,
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
        FormRow {
            FormNumberSelector(
                value = state.birthYear.first,
                label = "Min birth year",
                range = filter.birthYear,
                onUp = { update { copy(birthYear = it..max(it, birthYear.last)) } },
                onDown = { update { copy(birthYear = it..birthYear.last) } }
            )
            FormNumberSelector(
                value = state.birthYear.last,
                label = "Max birth year",
                range = filter.birthYear,
                onUp = { update { copy(birthYear = birthYear.first..it) } },
                onDown = { update { copy(birthYear = min(it, birthYear.first)..it) } }
            )
        }
        FormTextField(value = state.club, label = "Club") { update { copy(club = it) } }
        FormRow {
            FormNumberSelector(
                value = state.medals.first,
                label = "Min medals",
                range = filter.medals,
                onUp = { update { copy(medals = it..max(it, medals.last)) } },
                onDown = { update { copy(medals = it..medals.last) } }
            )
            FormNumberSelector(
                value = state.medals.last,
                label = "Max medals",
                range = filter.medals,
                onUp = { update { copy(medals = medals.first..it) } },
                onDown = { update { copy(medals = min(it, medals.first)..it) } }
            )
        }
    }
}

@Composable
internal fun BoxScope.AddOrUpdatePlayerOverlay(
    visible: Boolean,
    player: Player? = null,
    clubSuggestions: List<String>,
    onGetClubSuggestions: (String) -> Unit,
    onConfirm: (Player) -> Unit,
    dismissOverlay: () -> Unit
) {
    AddOrUpdateFormOverlay(
       defaultItemState = Player(),
       itemState = player,
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

        FormDropDownTextField(
            text = state.birthYear.toString(),
            label = "Birth year",
            readOnly = true,
            dropDownItems = (1900..2050).toList(),
            dropDownItemText = { Text(it.toString()) },
            onItemClicked = { update { copy(birthYear = it) } }
        )

        FormDropDownTextField(
            text = state.club,
            label = "Club",
            onTextChange = {
                update { copy(club = it) }
                onGetClubSuggestions(it)
            },
            dropDownItems = clubSuggestions,
            dropDownItemText = { Text(it) },
            onItemClicked = { update { copy(club = it) } }
        )
    }
}

@Composable
internal fun PlayerTable(
    state: ListState<Player, PlayerFilter, PlayerSorting>,
    onSort: (PlayerSorting) -> Unit,
    onDelete: (Player) -> Unit,
    onSelectPage: (Int) -> Unit,
    onNavigateTo: (View) -> Unit,
    onApplyFilter: (PlayerFilter) -> Unit
) {
    FilteredLazyTable(
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
    onSort: (PlayerSorting) -> Unit,
    onDelete: (Player) -> Unit
): List<LazyTableColumn<Player>> =
    listOf(
        LazyTableColumn.Link(name = "Name", weight = 0.25f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.NameDesc)
            }
        }) { navigateTo(View.Player(playerId = it.id)) },
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.BirthYearAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.BirthYearDesc)
            }
        }) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.25f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.ClubAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.ClubDesc)
            }
        }) { it.club },
        LazyTableColumn.Text(name = "# tournaments", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.TournamentsAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.TournamentsDesc)
            }
        }) { it.numberOfTournaments.toString() },
        LazyTableColumn.Builder(name = "Medals", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.MedalsAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.MedalsDesc)
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
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.SinglesAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.SinglesDesc)
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
                LazyTableSortDirection.Ascending -> onSort(PlayerSorting.DoublesAsc)
                LazyTableSortDirection.Descending -> onSort(PlayerSorting.DoublesDesc)
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
private fun PlayerBody(
    state: PlayerState,
    navigateTo: (View) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(50.dp)) {
        ListPreviewBox(
            name = "Tournaments",
            isLoading = state.isLoading,
            items = state.tournaments,
            onNavigateMore = { navigateTo(View.Tournaments) }
        ) {
            ListPreviewBoxLink(
                text = it.name,
                subText = "(${it.start.toFormattedString()} to ${it.end.toFormattedString()})"
            ) { navigateTo(View.Tournament(tournamentName = it.name, tournamentId = it.id)) }
        }

        ListPreviewBox(
            name = "Categories",
            isLoading = state.isLoading,
            items = state.categories,
            onNavigateMore = { navigateTo(View.Tournaments) }
        ) {
            ListPreviewBoxLink(
                text = it.name,
                subText = "(${it.tournamentName})"
            ) { navigateTo(View.Tournament(tournamentName = it.name, tournamentId = it.id)) }
        }

        ListPreviewBox(
            name = "Games",
            isLoading = state.isLoading,
            items = state.games,
            onNavigateMore = { navigateTo(View.Tournaments) }
        ) {
            ListPreviewBoxLink(
                text = it.playerNameLeftOne,
                subText = "()"
            ) {  }
        }
    }
}

/*private fun columns(
    navigateTo: (View) -> Unit,
    onDateSort: () -> Unit,
    onTournamentSort: () -> Unit,
    onCategorySort: () -> Unit,
    playerId: Long
): List<LazyTableColumn<Game>> =
    listOf(
        LazyTableColumn.Text(name = "Date", weight = 0.08f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onDateSort()
                LazyTableSortDirection.Descending -> onDateSort()
            }
        }) { it.submitted?.toFormattedString() ?: "N/A" },
        LazyTableColumn.Builder(name = "Tournament", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onTournamentSort()
                LazyTableSortDirection.Descending -> onTournamentSort()
            }
        }) { game, weight ->
            Row(
                modifier = Modifier.weight(weight),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(game.tournamentName)
                Link("(${game.categoryName})") {
                    View.Category(
                        categoryId = game.categoryId,
                        categoryName = game.categoryName,
                        tournamentId = game.tournamentId
                    )
                }
            }
        },
        LazyTableColumn.Text(name = "Game Rule", weight = 0.08f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onCategorySort()
                LazyTableSortDirection.Descending -> onCategorySort()
            }
        }) { it.ruleName },
        LazyTableColumn.Builder(name = "Left", weight = 0.1f) { game, weight ->
            Column(modifier = Modifier.weight(weight)) {
                if (game.playerLeftOneId == playerId)
                    Text(game.playerLeftOneName)
                else
                    Link(game.playerLeftOneName) {
                        navigateTo(View.Player(playerId = game.playerLeftOneId, playerName = game.playerLeftOneName))
                    }

                val playerLeftTwoId = game.playerLeftTwoId
                val playerLeftTwoName = game.playerLeftTwoName
                if (playerLeftTwoId != null && playerLeftTwoName != null) {
                    if (playerLeftTwoId == playerId)
                        Text(playerLeftTwoName)
                    else
                        Link(playerLeftTwoName) {
                            navigateTo(View.Player(playerId = playerLeftTwoId, playerName = playerLeftTwoName))
                        }
                }
            }
        },
        LazyTableColumn.Builder(name = "Results", weight = 0.05f) { game, weight ->
            val playerIsLeft = game.playerLeftOneId == playerId || game.playerLeftTwoId == playerId

            Column(modifier = Modifier.weight(weight)) {
                game.results.forEach {
                    Row {
                        Text(it.first.toString(), fontWeight = if (playerIsLeft) FontWeight.Bold else null)
                        Text(":")
                        Text(it.second.toString(), fontWeight = if (!playerIsLeft) FontWeight.Bold else null)
                    }
                }
            }
        },
        LazyTableColumn.Builder(name = "Right", weight = 0.1f) { game, weight ->
            Column(modifier = Modifier.weight(weight)) {
                if (game.playerRightOneId == playerId)
                    Text(game.playerRightOneName)
                else
                    Link(game.playerRightOneName) {
                        navigateTo(View.Player(playerId = game.playerRightOneId, playerName = game.playerRightOneName))
                    }

                val playerRightTwoId = game.playerRightTwoId
                val playerRightTwoName = game.playerRightTwoName
                if (playerRightTwoId != null && playerRightTwoName != null) {
                    if (playerRightTwoId == playerId)
                        Text(playerRightTwoName)
                    else
                        Link(playerRightTwoName) {
                            navigateTo(View.Player(playerId = playerRightTwoId, playerName = playerRightTwoName))
                        }
                }
            }
        },
        LazyTableColumn.Builder(name = "Game", weight = 0.05f) { game, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = game.totalGamePoints.first,
                right = game.totalGamePoints.second
            )
        },
        LazyTableColumn.Builder(name = "Sets", weight = 0.05f) { game, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = game.totalSetPoints.first,
                right = game.totalSetPoints.second
            )
        },
        LazyTableColumn.Builder(name = "Points", weight = 0.05f) { game, weight ->
            RatioBar(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 5.dp),
                left = game.totalPointPoints.first,
                right = game.totalPointPoints.second
            )
        }
    )*/

@Composable
private fun PlayerInfo(
    isLoading: Boolean,
    player: Player
) {
    Details(
        isLoading = isLoading,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        DetailSectionRow(title = player.name) {
            DetailText(title = "Birth year", text = player.birthYear.toString())
            DetailText(title = "Club", text = player.club)
        }

        DetailSectionColumn(title = "Statistics") {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                DetailText(title = "First game", text = player.firstGameDate?.toFormattedString() ?: "N/A")
                DetailText(title = "Last game", text = player.lastGameDate?.toFormattedString() ?: "N/A")
            }
        }
    }
}