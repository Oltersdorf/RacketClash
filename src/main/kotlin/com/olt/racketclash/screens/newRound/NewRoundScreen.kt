package com.olt.racketclash.screens.newRound

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Player
import com.olt.racketclash.language.translations.Language
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class NewRoundScreen(private val modelBuilder: () -> NewRoundModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            language = stateModel.language,
            topAppBarTitle = stateModel.language.newRound,
            hasBackPress = true,
            selectedTab = TournamentTabs.Games(language = stateModel.language),
            navigateTo = screenModel::navigateTo
        ) {
            SettingsView {
                NewRoundView(model = stateModel, screenModel = screenModel)
            }
        }
    }
}

@Composable
private fun NewRoundView(
    model: NewRoundModel.Model,
    screenModel: NewRoundModel
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = model.roundName,
        onValueChange = { screenModel.changeRoundName(newName = it) },
        label = { Text(model.language.name) },
        enabled = !model.generating,
        isError = model.roundName.isBlank()
    )

    DropDownMenu(
        modifier = Modifier.fillMaxWidth(),
        label = model.language.type,
        items = model.roundTypes,
        value = model.selectedRoundType,
        textMapper = { roundTypeToString(language = model.language, roundType = it) },
        onClick = screenModel::changeRoundType
    )

    when (model.selectedRoundType) {
        NewRoundModel.RoundType.Empty ->
            Empty(model = model, screenModel = screenModel)
        is NewRoundModel.RoundType.EquallyStrongDouble ->
            EquallyStrongDouble(model = model, screenModel = screenModel, roundType = model.selectedRoundType)
    }
}

@Composable
private fun CancelSaveButtonRow(
    model: NewRoundModel.Model,
    screenModel: NewRoundModel,
    onSave: () -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    CancelSaveButtonRow(
        language = model.language,
        onCancel = { screenModel.navigateTo(Screens.Games(language = model.language), navigator) },
        canSave = model.canCreate && !model.generating,
        onSave = {
            onSave()
            screenModel.navigateTo(Screens.Games(language = model.language), navigator)
        }
    )
}

@Composable
private fun Empty(
    model: NewRoundModel.Model,
    screenModel: NewRoundModel
) {
    CancelSaveButtonRow(model = model, screenModel = screenModel, onSave = screenModel::addEmptyRound)
}

@Composable
private fun EquallyStrongDouble(
    model: NewRoundModel.Model,
    screenModel: NewRoundModel,
    roundType: NewRoundModel.RoundType.EquallyStrongDouble
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        NumberSelector(
            enabled = !model.generating,
            label = model.language.rounds + ":",
            value = roundType.rounds,
            onValueChange = screenModel::changeEquallyStrongDoublesRounds,
            min = 1
        )

        Checkbox(
            enabled = !model.generating,
            checked = roundType.differentPartnersEachRound,
            onCheckedChange = screenModel::changeEquallyStrongDoublesDifferentPartners
        )
        Text(model.language.differentPartnersEachRound)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            enabled = !model.generating,
            checked = roundType.tryUntilNoMoreThanOneByePerPerson,
            onCheckedChange = screenModel::changeTryUntilNoMoreThanOneByePerPerson
        )
        Text(model.language.onlyOneByePerPerson)

        Checkbox(
            enabled = !model.generating,
            checked = roundType.tryUntilWorstPerformanceIsZero,
            onCheckedChange = screenModel::changeTryUntilStrengthDifferenceIsZero
        )
        Text(model.language.worstStrengthDifferenceIsZero)
    }

    NumberSelector(
        enabled = !model.generating,
        label = model.language.maxRepeats + ":",
        value = roundType.maxRepeat,
        onValueChange = screenModel::changeEquallyStrongDoublesMaxRepeats,
        min = 1
    )

    LazyTableWithScrollScaffold(
        topBarTitle = model.language.players,
        topBarActions = {
            TextField(
                modifier = Modifier.width(TextFieldDefaults.MinWidth),
                value = model.filter,
                onValueChange = screenModel::changeFilter,
                label = { Text(model.language.filterByName) },
                singleLine = true
            )

            DropDownMenu(
                modifier = Modifier.padding(start = 5.dp).width(TextFieldDefaults.MinWidth),
                label = model.language.sortBy,
                items = Player.sortingOptions(),
                value = model.sortedBy,
                textMapper = { it.text(language = model.language) },
                onClick = screenModel::changeSorting
            )
        },
        items = model.players,
        columns = listOf(
            LazyTableColumn.Checkbox(
                name = model.language.active,
                weight = 1.0f,
                checked = { it.active },
                onCheckChanged = { item, checked -> screenModel.updateActive(item.id, checked) }
            ),
            LazyTableColumn.Text(
                name = model.language.name,
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = model.language.team,
                weight = 2.0f,
                text = { it.teamName }
            ),
            LazyTableColumn.Text(
                name = model.language.pending,
                weight = 1.0f,
                text = { it.openGames.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.played,
                weight = 1.0f,
                text = { it.played.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.byes,
                weight = 1.0f,
                text = { it.bye.toString() }
            ),
            LazyTableColumn.Text(
                name = model.language.games,
                weight = 1.0f,
                text = { "${it.wonGames} : ${it.lostGames}" }
            ),
            LazyTableColumn.Text(
                name = model.language.sets,
                weight = 1.0f,
                text = { "${it.wonSets} : ${it.lostSets}" }
            ),
            LazyTableColumn.Text(
                name = model.language.points,
                weight = 1.0f,
                text = { "${it.wonPoints} : ${it.lostPoints}" }
            )
        )
    )

    if (model.generating)
        Loading()
    else if(roundType.games.isNotEmpty()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${roundType.games.size} ${model.language.gamesGenerated}")
            Text("${model.language.worstStrengthDifferenceIs} ${roundType.performance}")
            Text(if (roundType.byePlayer.isNotBlank()) "${model.language.byes}: ${roundType.byePlayer}" else "")
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Spacer(modifier = Modifier.weight(1.0f))
        Button(
            onClick = screenModel::generateEquallyStrongDoubles,
            enabled = !model.generating
        ) {
            Text(model.language.generate)
        }

        val navigator = LocalNavigator.currentOrThrow
        Button(
            onClick = {
                screenModel.addEquallyStrongDoubles()
                screenModel.navigateTo(screen = Screens.Games(language = model.language), navigator = navigator)
            },
            enabled = roundType.games.isNotEmpty() && model.canCreate
        ) {
            Text(model.language.save)
        }
    }
}

private fun roundTypeToString(language: Language, roundType: NewRoundModel.RoundType) : String =
    when (roundType) {
        NewRoundModel.RoundType.Empty -> language.empty
        is NewRoundModel.RoundType.EquallyStrongDouble -> language.equallyStrongDoubles
    }