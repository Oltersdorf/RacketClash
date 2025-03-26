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
import com.olt.racketclash.database.api.*
import com.olt.racketclash.schedule.ScheduleModel
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.ui.material.*
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableSortDirection
import com.olt.racketclash.ui.base.material.SimpleIconButton
import com.olt.racketclash.ui.layout.FilteredLazyTable

@Composable
internal fun Schedule(
    database: Database,
    tournamentId: Long
) {
    val model = remember { ScheduleModel(database = database.schedules, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    FilteredLazyTable(
        state = ListState(
            isLoading = state.isLoading,
            items = state.scheduledGames,
            filter = ScheduleFilter(),
            sorting = ScheduleSorting.ScheduleAsc
        ),
        columns = columns(
            onConfirm = model::onSaveResult,
            onSort = model::onSort
        ),
        onPageClicked = model::updatePage
    ) {

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