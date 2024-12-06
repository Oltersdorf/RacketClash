package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.addorupdateteam.AddOrUpdateTeamModel
import com.olt.racketclash.database.Database
import com.olt.racketclash.database.player.Sorting
import com.olt.racketclash.database.table.FilteredAndOrderedPlayer
import com.olt.racketclash.ui.component.RatioBar
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.component.SearchBarMenuItem
import com.olt.racketclash.ui.component.SearchBarTagChip
import com.olt.racketclash.ui.layout.*
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.generated.resources.Res
import racketclash.common.ui.generated.resources.medal

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AddOrUpdateTeam(
    database: Database,
    teamId: Long?,
    teamName: String?,
    tournamentId: Long,
    navigateBack: () -> Unit
) {
    val model = remember { AddOrUpdateTeamModel(
        database = database,
        teamId = teamId,
        tournamentId = tournamentId
    ) }
    val state by model.state.collectAsState()

    Form(
        title = teamName ?: "New team",
        isLoading = state.isLoading,
        isSavable = state.isSavable,
        onSave = { model.save(onComplete = navigateBack) }
    ) {
        FormRow {
            FormTextField(
                value = state.team.name,
                label = "Name",
                isError = !state.isSavable,
                onValueChange = model::updateName
            )

            FormNumberSelector(
                value = state.team.rank,
                label = "Rank",
                range = 1..Int.MAX_VALUE,
                onUp = model::updateRank,
                onDown = model::updateRank
            )
        }

        FormTable(
            title = "Players",
            items = state.players,
            columns = columns(
                onSort = model::onSort,
                selectedPlayers = state.selectedPlayers,
                onAddSelect = model::onAddSelect,
                onRemoveSelect = model::onRemoveSelect
            ),
            isLoading = state.playersLoading,
            currentPage = state.currentPage,
            lastPage = state.lastPage,
            onPageClicked = model::changePage
        ) {
            SearchBar(
                text = state.searchBarText,
                onTextChange = model::updateSearchBar,
                dropDownItems = {
                    state.availableTags.name?.let {
                        SearchBarMenuItem(name = "Name", text = it, onClick = model::addNameTag)
                    }
                    state.availableTags.birthYear?.let {
                        SearchBarMenuItem(name = "Birth year", text = it.toString(), onClick = model::addBirthYearTag)
                    }
                    state.availableTags.club?.let {
                        SearchBarMenuItem(name = "Club", text = it, onClick = model::addClubTag)
                    }
                    state.availableTags.hasMedals?.let {
                        SearchBarMenuItem(name = "Has medals") { model.addHasMedalsTag(value = true) }
                        SearchBarMenuItem(name = "Has no medals") { model.addHasMedalsTag(value = false) }
                    }
                }
            ) {
                state.tags.name?.let {
                    SearchBarTagChip(name = "Name", text = it, onRemove = model::removeNameTag)
                }
                state.tags.birthYear?.let {
                    SearchBarTagChip(name = "Birth year", text = it.toString(), onRemove = model::removeBirthYearTag)
                }
                state.tags.club?.let {
                    SearchBarTagChip(name = "Club", text = it, onRemove = model::removeClubTag)
                }
                state.tags.hasMedals?.let {
                    if (it)
                        SearchBarTagChip(name = "Has medals", onRemove = model::removeHasMedalsTag)
                    else
                        SearchBarTagChip(name = "Has no medals", onRemove = model::removeHasMedalsTag)
                }
            }
        }
    }
}

private fun columns(
    onSort: (Sorting) -> Unit,
    selectedPlayers: Set<Long>,
    onAddSelect: (Long) -> Unit,
    onRemoveSelect: (Long) -> Unit
): List<LazyTableColumn<FilteredAndOrderedPlayer>> =
    listOf(
        LazyTableColumn.Checkbox(
            name = "Selected",
            weight = 0.1f,
            checked = { selectedPlayers.contains(it.id) }
        ) { player, checked ->
            if (checked)
                onAddSelect(player.id)
            else
                onRemoveSelect(player.id)
        },
        LazyTableColumn.Text(name = "Name", weight = 0.25f, text = { it.name }, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.NameAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.NameDesc)
            }
        }),
        LazyTableColumn.Text(name = "Birth year", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.BirthYearAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.BirthYearDesc)
            }
        }) { it.birthYear.toString() },
        LazyTableColumn.Text(name = "Club", weight = 0.25f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.ClubAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.ClubDesc)
            }
        }) { it.club },
        LazyTableColumn.Text(name = "# tournaments", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.TournamentsAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.TournamentsDesc)
            }
        }) { it.numberOfTournaments.toString() },
        LazyTableColumn.Builder(name = "Medals", weight = 0.1f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(Sorting.MedalsAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.MedalsDesc)
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
                LazyTableSortDirection.Ascending -> onSort(Sorting.SinglesAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.SinglesDesc)
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
                LazyTableSortDirection.Ascending -> onSort(Sorting.DoublesAsc)
                LazyTableSortDirection.Descending -> onSort(Sorting.DoublesDesc)
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
        }
    )