package com.olt.racketclash.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.database.api.GameSet
import com.olt.racketclash.database.api.Schedule
import com.olt.racketclash.database.api.ScheduleSorting
import com.olt.racketclash.schedule.ScheduleModel
import com.olt.racketclash.ui.material.*
import com.olt.racketclash.ui.layout.LazyTableColumn
import com.olt.racketclash.ui.layout.LazyTableSortDirection
import com.olt.racketclash.ui.layout.SearchableLazyTableWithScroll

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Schedule(
    database: Database,
    tournamentId: Long
) {
    val model = remember { ScheduleModel(database = database.schedules, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    SearchableLazyTableWithScroll(
        title = "Schedule",
        items = state.scheduledGames,
        isLoading = state.isLoading,
        columns = columns(
            onConfirm = model::onSaveResult,
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
                state.availableTags.active?.let {
                    SearchBarMenuItem(name = "Active") { model.addActiveTag(true) }
                    SearchBarMenuItem(name = "Not active") { model.addActiveTag(false) }
                }
                state.availableTags.singles?.let {
                    SearchBarMenuItem(name = "Is single") { model.addSinglesTag(true) }
                    SearchBarMenuItem(name = "is double") { model.addSinglesTag(false) }
                }
                state.availableTags.category?.let {
                    SearchBarMenuItem(name = "Category", text = it, onClick = model::addCategoryTag)
                }
                state.availableTags.player?.let {
                    SearchBarMenuItem(name = "Player", text = it, onClick = model::addPlayerTag)
                }
            }
        ) {
            state.tags.active?.let {
                if (it)
                    SearchBarTagChip(name = "Active", onRemove = model::removeActiveTag)
                else
                    SearchBarTagChip(name = "Not active", onRemove = model::removeActiveTag)
            }
            state.tags.singles?.let {
                if (it)
                    SearchBarTagChip(name = "Is single", onRemove = model::removeSinglesTag)
                else
                    SearchBarTagChip(name = "Is double", onRemove = model::removeSinglesTag)
            }
            state.tags.category?.let {
                SearchBarTagChip(name = "Category", text = it, onRemove = model::removeCategoryTag)
            }
            state.tags.player?.let {
                SearchBarTagChip(name = "Player", text = it, onRemove = model::removePlayerTag)
            }
        }
    }
}

private fun columns(
    onConfirm: (Long, List<GameSet>) -> Unit,
    onSort: (ScheduleSorting) -> Unit
): List<LazyTableColumn<Schedule>> =
    listOf(
        LazyTableColumn.Builder("Active", weight = 0.05f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(ScheduleSorting.ActiveAsc)
                LazyTableSortDirection.Descending -> onSort(ScheduleSorting.ActiveDesc)
            }
        }) { scheduledGame, weight ->
            Status(modifier = Modifier.weight(weight), isOkay = scheduledGame.active)
        },
        LazyTableColumn.Text(name = "Schedule", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(ScheduleSorting.ScheduleAsc)
                LazyTableSortDirection.Descending -> onSort(ScheduleSorting.ScheduleDesc)
            }
        }) { it.scheduledFor.toString() },
        LazyTableColumn.Text(name = "Type", weight = 0.05f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(ScheduleSorting.TypeAsc)
                LazyTableSortDirection.Descending -> onSort(ScheduleSorting.TypeDesc)
            }
        }) { if (it.playerIdLeftTwo == null) "Single" else "Double" },
        LazyTableColumn.Text(name = "Category", weight = 0.15f, onSort = {
            when (it) {
                LazyTableSortDirection.Ascending -> onSort(ScheduleSorting.CategoryAsc)
                LazyTableSortDirection.Descending -> onSort(ScheduleSorting.CategoryDesc)
            }
        }) { it.categoryName },
        LazyTableColumn.Builder(name = "Left", weight = 0.2f) { scheduledGame, weight ->
            Column(modifier = Modifier.weight(weight)) {
                Text(scheduledGame.playerNameLeftOne)
                val leftTwo = scheduledGame.playerNameLeftTwo
                if (leftTwo != null) Text(leftTwo)
            }
        },
        LazyTableColumn.Builder(name = "Result", weight = 0.2f) { scheduledGame, weight ->
            Column(modifier = Modifier.weight(weight)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var sets by remember { mutableStateOf(listOf<GameSet>()) }

                    Column {
                        sets.forEachIndexed { setIndex, set ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    value = set.leftPoints.toString(),
                                    onValueChange = {
                                        val number = it.toIntOrNull()
                                        if (number != null)
                                            sets = sets.mapIndexed { index, set ->
                                                if (index == setIndex)
                                                    set.copy(leftPoints = number)
                                                else
                                                    set
                                            }
                                    },
                                    singleLine = true
                                )

                                Text(text = ":", modifier = Modifier.padding(horizontal = 5.dp))

                                OutlinedTextField(
                                    modifier = Modifier.width(100.dp),
                                    value = set.rightPoints.toString(),
                                    onValueChange = {
                                        val number = it.toIntOrNull()
                                        if (number != null)
                                            sets = sets.mapIndexed { index, set ->
                                                if (index == setIndex)
                                                    set.copy(rightPoints = number)
                                                else
                                                    set
                                            }
                                    },
                                    singleLine = true
                                )
                            }
                        }
                    }

                    SimpleIconButton(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Confirm"
                    ) { onConfirm(scheduledGame.id, sets) }
                }
            }
        },
        LazyTableColumn.Builder(name = "Right", weight = 0.2f) { scheduledGame, weight ->
            Column(modifier = Modifier.weight(weight)) {
                Text(scheduledGame.playerNameRightOne)
                val rightTwo = scheduledGame.playerNameRightTwo
                if (rightTwo != null) Text(rightTwo)
            }
        }
    )