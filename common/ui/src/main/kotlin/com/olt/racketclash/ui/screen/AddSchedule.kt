package com.olt.racketclash.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.olt.racketclash.addschedule.AddScheduleModel
import com.olt.racketclash.addschedule.Player
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.Loading
import com.olt.racketclash.ui.component.SearchBar
import com.olt.racketclash.ui.layout.*

@Composable
internal fun AddSchedule(
    database: Database,
    categoryId: Long,
    categoryName: String,
    tournamentId: Long,
    navigateBack: () -> Unit
) {
    val model = remember { AddScheduleModel(database = database, categoryId = categoryId, tournamentId = tournamentId) }
    val state by model.state.collectAsState()

    Form(
        title = categoryName,
        isLoading = state.isLoading,
        confirmButton = {
            FormButton(
                text = "Save",
                enabled = state.isSavable
            ) { model.onSave(callback = navigateBack) }
        }
    ) {
        FormRow {
            FormNumberSelector(
                value = state.rounds,
                label = "Rounds",
                range = 1..Int.MAX_VALUE,
                onUp = model::updateRounds,
                onDown = model::updateRounds
            )

            FormNumberSelector(
                value = state.maxRepeats,
                label = "max repeats",
                range = 1..Int.MAX_VALUE,
                onUp = model::updateMaxRepeats,
                onDown = model::updateMaxRepeats
            )
        }

        FormRow {
            FormCheckBox(
                text = "Different partners each round",
                checked = state.differentPartnersEachRound,
                onCheckChanged = model::updateDifferentPartnersEachRound
            )

            FormCheckBox(
                text = "Only one rest per player",
                checked = state.onlyOneRestPerPlayer,
                onCheckChanged = model::updateOnlyOneRestPerPlayer
            )

            FormCheckBox(
                text = "Worst strength difference is 0",
                checked = state.worstStrengthDifferenceIsZero,
                onCheckChanged = model::updateWorstStrengthDifferenceIsZero
            )
        }
        
        FormTable(
            title = "Players",
            items = state.players,
            currentPage = state.currentPage,
            lastPage = state.lastPage,
            onPageClicked = model::updatePage,
            columns = listOf(
                LazyTableColumn.Checkbox(
                    name = "Selected",
                    weight = 0.1f,
                    checked = { state.selectedPlayers.contains(it.id) },
                    onCheckChanged = { item, checked -> model.updateActive(item.id, checked) }
                ),
                LazyTableColumn.Text(name = "Name", weight = 0.45f) { it.name },
                LazyTableColumn.Text(name = "Team", weight = 0.45f) { it.team }
            )
        ) {
            var searchBarText by remember { mutableStateOf("") }

            SearchBar(
                text = searchBarText,
                onTextChange = { searchBarText = it },
                dropDownItems = emptyList<Player>(),
                tags = emptyList(),
                onDropDownItemClick = {},
                onTagRemove = {},
                tagText = {}
            )
        }

        FormButton(text = "Generate", onClick = model::generate)

        if (state.isGenerating)
            Loading()
        else if (state.generatedGames.isNotEmpty()) {
            Column {
                Text("${state.generatedGames.size} games generated")
                Text("Worst strength difference is ${state.generatedPerformance}")
                if (state.generatedRests.isNotEmpty())
                    Text("Rests: ${state.generatedRests.joinToString { it.name }}")
            }
        }
    }
}