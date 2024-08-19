package com.olt.racketclash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.data.Player
import com.olt.racketclash.language.Language
import com.olt.racketclash.navigate.Screens
import com.olt.racketclash.newRound.NewRoundModel
import com.olt.racketclash.ui.*

@Composable
fun NewRoundScreen(
    model: NewRoundModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    val state by model.state.collectAsState()

    TournamentScaffold(
        language = language,
        projectId = state.projectId,
        topAppBarTitle = language.newRound,
        hasBackPress = true,
        selectedTab = TournamentTabs.Games(language = language, projectId = state.projectId),
        navigateTo = { navigateTo(it) }
    ) {
        SettingsView {
            NewRoundView(state = state, model = model, language = language, navigateTo = navigateTo)
        }
    }
}

@Composable
private fun NewRoundView(
    state: NewRoundModel.State,
    model: NewRoundModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.roundName,
        onValueChange = { model.changeRoundName(newName = it) },
        label = { Text(language.name) },
        enabled = !state.generating,
        isError = state.roundName.isBlank()
    )

    DropDownMenu(
        modifier = Modifier.fillMaxWidth(),
        label = language.type,
        items = state.roundTypes,
        value = state.selectedRoundType,
        textMapper = { roundTypeToString(language = language, roundType = it) },
        onClick = model::changeRoundType
    )

    when (state.selectedRoundType) {
        NewRoundModel.RoundType.Empty ->
            Empty(state = state, model = model, language = language, navigateTo = navigateTo)
        is NewRoundModel.RoundType.EquallyStrongDouble ->
            EquallyStrongDouble(
                state = state,
                model = model,
                language = language,
                roundType = state.selectedRoundType as NewRoundModel.RoundType.EquallyStrongDouble,
                navigateTo = navigateTo
            )
    }
}

@Composable
private fun CancelSaveButtonRow(
    state: NewRoundModel.State,
    language: Language,
    navigateTo: (Screens) -> Unit,
    onSave: () -> Unit
) {
    CancelSaveButtonRow(
        language = language,
        onCancel = { navigateTo(Screens.Games(projectId = state.projectId)) },
        canSave = state.canCreate && !state.generating,
        onSave = {
            onSave()
            navigateTo(Screens.Games(projectId = state.projectId))
        }
    )
}

@Composable
private fun Empty(
    state: NewRoundModel.State,
    model: NewRoundModel,
    language: Language,
    navigateTo: (Screens) -> Unit
) {
    CancelSaveButtonRow(
        state = state,
        language = language,
        navigateTo = navigateTo,
        onSave = model::addEmptyRound
    )
}

@Composable
private fun EquallyStrongDouble(
    state: NewRoundModel.State,
    model: NewRoundModel,
    language: Language,
    roundType: NewRoundModel.RoundType.EquallyStrongDouble,
    navigateTo: (Screens) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        NumberSelector(
            enabled = !state.generating,
            label = language.rounds + ":",
            value = roundType.rounds,
            onValueChange = model::changeEquallyStrongDoublesRounds,
            min = 1
        )

        Checkbox(
            enabled = !state.generating,
            checked = roundType.differentPartnersEachRound,
            onCheckedChange = model::changeEquallyStrongDoublesDifferentPartners
        )
        Text(language.differentPartnersEachRound)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            enabled = !state.generating,
            checked = roundType.tryUntilNoMoreThanOneByePerPerson,
            onCheckedChange = model::changeTryUntilNoMoreThanOneByePerPerson
        )
        Text(language.onlyOneByePerPlayer)

        Checkbox(
            enabled = !state.generating,
            checked = roundType.tryUntilWorstPerformanceIsZero,
            onCheckedChange = model::changeTryUntilStrengthDifferenceIsZero
        )
        Text(language.worstStrengthDifferenceIsZero)
    }

    NumberSelector(
        enabled = !state.generating,
        label = language.maxRepeats + ":",
        value = roundType.maxRepeat,
        onValueChange = model::changeEquallyStrongDoublesMaxRepeats,
        min = 1
    )

    LazyTableWithScrollScaffold(
        topBarTitle = language.players,
        topBarActions = {
            TextField(
                modifier = Modifier.width(TextFieldDefaults.MinWidth),
                value = state.filter,
                onValueChange = model::changeFilter,
                label = { Text(language.filterByName) },
                singleLine = true
            )

            DropDownMenu(
                modifier = Modifier.padding(start = 5.dp).width(TextFieldDefaults.MinWidth),
                label = language.sortBy,
                items = Player.sortingOptions(),
                value = state.sortedBy,
                textMapper = { it.text(language = language) },
                onClick = model::changeSorting
            )
        },
        items = state.players,
        columns = listOf(
            LazyTableColumn.Checkbox(
                name = language.active,
                weight = 1.0f,
                checked = { it.active },
                onCheckChanged = { item, checked -> model.updateActive(item.id, checked) }
            ),
            LazyTableColumn.Text(
                name = language.name,
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = language.team,
                weight = 2.0f,
                text = { it.teamName }
            ),
            LazyTableColumn.Text(
                name = language.pending,
                weight = 1.0f,
                text = { it.openGames.toString() }
            ),
            LazyTableColumn.Text(
                name = language.played,
                weight = 1.0f,
                text = { it.played.toString() }
            ),
            LazyTableColumn.Text(
                name = language.byes,
                weight = 1.0f,
                text = { it.bye.toString() }
            ),
            LazyTableColumn.Text(
                name = language.games,
                weight = 1.0f,
                text = { "${it.wonGames} : ${it.lostGames}" }
            ),
            LazyTableColumn.Text(
                name = language.sets,
                weight = 1.0f,
                text = { "${it.wonSets} : ${it.lostSets}" }
            ),
            LazyTableColumn.Text(
                name = language.points,
                weight = 1.0f,
                text = { "${it.wonPoints} : ${it.lostPoints}" }
            )
        )
    )

    if (state.generating)
        Loading()
    else if(roundType.games.isNotEmpty()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${roundType.games.size} ${language.gamesGenerated}")
            Text("${language.worstStrengthDifferenceIs} ${roundType.performance}")
            Text(if (roundType.byePlayer.isNotBlank()) "${language.byes}: ${roundType.byePlayer}" else "")
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Spacer(modifier = Modifier.weight(1.0f))
        Button(
            onClick = model::generateEquallyStrongDoubles,
            enabled = !state.generating
        ) {
            Text(language.generate)
        }

        Button(
            onClick = {
                model.addEquallyStrongDoubles()
                navigateTo(Screens.Games(projectId = state.projectId))
            },
            enabled = roundType.games.isNotEmpty() && state.canCreate
        ) {
            Text(language.save)
        }
    }
}

private fun roundTypeToString(language: Language, roundType: NewRoundModel.RoundType) : String =
    when (roundType) {
        NewRoundModel.RoundType.Empty -> language.empty
        is NewRoundModel.RoundType.EquallyStrongDouble -> language.equallyStrongDoubles
    }