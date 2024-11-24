package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.rackeclash.addorupdaterule.AddOrUpdateRuleModel
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.layout.Form
import com.olt.racketclash.ui.layout.FormNumberSelector
import com.olt.racketclash.ui.layout.FormRow
import com.olt.racketclash.ui.layout.FormTextField

@Composable
internal fun AddOrUpdateRule(
    database: Database,
    ruleId: Long?,
    ruleName: String?,
    navigateBack: () -> Unit
) {
    val model = remember { AddOrUpdateRuleModel(database = database, ruleId = ruleId) }
    val state by model.state.collectAsState()

    Form(
        title = ruleName ?: "New rule",
        isLoading = state.isLoading,
        isSavable = state.isSavable,
        onSave = {
            model.save()
            navigateBack()
        }
    ) {
        FormTextField(
            value = state.rule.name,
            label = "Name",
            isError = !state.isSavable,
            onValueChange = model::updateName
        )

        FormRow {
            FormNumberSelector(
                value = state.rule.maxSets,
                label = "Max sets",
                range = 1..Int.MAX_VALUE,
                onUp = model::maxSetsUp,
                onDown = model::maxSetsDown
            )

            FormNumberSelector(
                value = state.rule.winSets,
                label = "Win sets",
                range = 1..Int.MAX_VALUE,
                onUp = model::winSetsUp,
                onDown = model::winSetsDown
            )
        }

        FormRow {
            FormNumberSelector(
                value = state.rule.maxPoints,
                label = "Max points",
                range = 1..Int.MAX_VALUE,
                onUp = model::maxPointsUp,
                onDown = model::maxPointsDown
            )

            FormNumberSelector(
                value = state.rule.winPoints,
                label = "Win points",
                range = 1..Int.MAX_VALUE,
                onUp = model::winPointsUp,
                onDown = model::winPointsDown
            )

            FormNumberSelector(
                value = state.rule.pointsDifference,
                label = "Points difference",
                range = 0..Int.MAX_VALUE,
                onUp = model::pointsDifferenceUp,
                onDown = model::pointsDifferenceDown
            )
        }

        FormRow {
            FormNumberSelector(
                value = state.rule.gamePointsForWin,
                label = "Game points for win",
                range = 0..Int.MAX_VALUE,
                onUp = model::updateGamePointsForWin,
                onDown = model::updateGamePointsForWin
            )

            FormNumberSelector(
                value = state.rule.gamePointsForLose,
                label = "Game points for lose",
                range = 0..Int.MAX_VALUE,
                onUp = model::updateGamePointsForLose,
                onDown = model::updateGamePointsForLose
            )

            FormNumberSelector(
                value = state.rule.gamePointsForDraw,
                label = "Game points for draw",
                range = 0..Int.MAX_VALUE,
                onUp = model::updateGamePointsForDraw,
                onDown = model::updateGamePointsForDraw
            )
        }

        FormRow {
            FormNumberSelector(
                value = state.rule.gamePointsForRest,
                label = "Game points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = model::updateGamePointsForRest,
                onDown = model::updateGamePointsForRest
            )

            FormNumberSelector(
                value = state.rule.setPointsForRest,
                label = "Set points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = model::updateSetPointsForRest,
                onDown = model::updateSetPointsForRest
            )

            FormNumberSelector(
                value = state.rule.pointPointsForRest,
                label = "Point points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = model::updatePointPointsForRest,
                onDown = model::updatePointPointsForRest
            )
        }
    }
}