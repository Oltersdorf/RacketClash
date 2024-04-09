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
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class NewRoundScreen(private val modelBuilder: () -> NewRoundModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        TournamentScaffold(
            topAppBarTitle = "New Round",
            hasBackPress = true,
            selectedTab = TournamentTabs.Games,
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
        label = { Text("Name") },
        enabled = !model.generating,
        isError = model.roundName.isBlank()
    )

    DropDownMenu(
        modifier = Modifier.fillMaxWidth(),
        label = "Type",
        items = model.roundTypes,
        value = model.selectedRoundType,
        textMapper = ::roundTypeToString,
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
        onCancel = { screenModel.navigateTo(Screens.Games, navigator) },
        canSave = model.canCreate && !model.generating,
        onSave = {
            onSave()
            screenModel.navigateTo(Screens.Games, navigator)
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
            label = "Rounds:",
            value = roundType.rounds,
            onValueChange = screenModel::changeEquallyStrongDoublesRounds,
            min = 1
        )

        Checkbox(
            enabled = !model.generating,
            checked = roundType.differentPartnersEachRound,
            onCheckedChange = screenModel::changeEquallyStrongDoublesDifferentPartners
        )
        Text("Different partners each round")
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            enabled = !model.generating,
            checked = roundType.tryUntilNoMoreThanOneByePerPerson,
            onCheckedChange = screenModel::changeTryUntilNoMoreThanOneByePerPerson
        )
        Text("Only one bye per person")

        Checkbox(
            enabled = !model.generating,
            checked = roundType.tryUntilWorstPerformanceIsZero,
            onCheckedChange = screenModel::changeTryUntilStrengthDifferenceIsZero
        )
        Text("Worst strength difference is zero")
    }

    NumberSelector(
        enabled = !model.generating,
        label = "Max repeats:",
        value = roundType.maxRepeat,
        onValueChange = screenModel::changeEquallyStrongDoublesMaxRepeats,
        min = 1
    )

    LazyTableWithScrollScaffold(
        topBarTitle = "Players",
        topBarActions = {
            TextField(
                modifier = Modifier.width(TextFieldDefaults.MinWidth),
                value = model.filter,
                onValueChange = screenModel::changeFilter,
                label = { Text("Filter by name") },
                singleLine = true
            )

            DropDownMenu(
                modifier = Modifier.padding(start = 5.dp).width(TextFieldDefaults.MinWidth),
                label = "Sort by",
                items = Player.sortingOptions(),
                value = model.sortedBy,
                textMapper = Player.Sorting::text,
                onClick = screenModel::changeSorting
            )
        },
        items = model.players,
        columns = listOf(
            LazyTableColumn.Checkbox(
                name = "Active",
                weight = 1.0f,
                checked = { it.active },
                onCheckChanged = { item, checked -> screenModel.updateActive(item.id, checked) }
            ),
            LazyTableColumn.Text(
                name = "Name",
                weight = 5.0f,
                text = { it.name }
            ),
            LazyTableColumn.Text(
                name = "Team",
                weight = 2.0f,
                text = { it.teamName }
            ),
            LazyTableColumn.Text(
                name = "pending",
                weight = 1.0f,
                text = { it.openGames.toString() }
            ),
            LazyTableColumn.Text(
                name = "played",
                weight = 1.0f,
                text = { it.played.toString() }
            ),
            LazyTableColumn.Text(
                name = "bye",
                weight = 1.0f,
                text = { it.bye.toString() }
            ),
            LazyTableColumn.Text(
                name = "Games",
                weight = 1.0f,
                text = { "${it.wonGames} : ${it.lostGames}" }
            ),
            LazyTableColumn.Text(
                name = "Sets",
                weight = 1.0f,
                text = { "${it.wonSets} : ${it.lostSets}" }
            ),
            LazyTableColumn.Text(
                name = "Points",
                weight = 1.0f,
                text = { "${it.wonPoints} : ${it.lostPoints}" }
            )
        )
    )

    if (model.generating)
        Loading()
    else if(roundType.games.isNotEmpty()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${roundType.games.size} games generated")
            Text("Worst strength difference is ${roundType.performance}")
            Text("Bye: ${roundType.byePlayer}")
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Spacer(modifier = Modifier.weight(1.0f))
        Button(
            onClick = screenModel::generateEquallyStrongDoubles,
            enabled = !model.generating
        ) {
            Text("Generate")
        }

        val navigator = LocalNavigator.currentOrThrow
        Button(
            onClick = {
                screenModel.addEquallyStrongDoubles()
                screenModel.navigateTo(screen = Screens.Games, navigator = navigator)
            },
            enabled = roundType.games.isNotEmpty() && model.canCreate
        ) {
            Text("Save")
        }
    }
}

private fun roundTypeToString(roundType: NewRoundModel.RoundType) : String =
    when (roundType) {
        NewRoundModel.RoundType.Empty -> "Empty"
        is NewRoundModel.RoundType.EquallyStrongDouble -> "Equally strong doubles"
    }