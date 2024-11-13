package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.layout.Form
import com.olt.racketclash.ui.layout.FormNumberSelector
import com.olt.racketclash.ui.layout.FormRow
import com.olt.racketclash.ui.layout.FormTextField
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
internal fun AddOrUpdateGameRule(
    database: Database,
    gameRuleId: Long?,
    gameRuleName: String?,
    navigateBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var isSavable by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var maxSets by remember { mutableStateOf(3) }
    var winSets by remember { mutableStateOf(2) }
    var maxPoints by remember { mutableStateOf(30) }
    var winPoints by remember { mutableStateOf(21) }
    var pointsDifference by remember { mutableStateOf(2) }
    var gamePointsForWin by remember { mutableStateOf(2) }
    var gamePointsForLose by remember { mutableStateOf(0) }
    var gamePointsForDraw by remember { mutableStateOf(1) }
    var gamePointsForRest by remember { mutableStateOf(2) }
    var setPointsForRest by remember { mutableStateOf(0) }
    var pointPointsForRest by remember { mutableStateOf(0) }

    Form(
        title = gameRuleName ?: "New game rule",
        isLoading = isLoading,
        isSavable = isSavable,
        onSave = {
            navigateBack()
        }
    ) {
        FormTextField(value = name, label = "Name", isError = !isSavable) {
            name = it
            isSavable = name.isNotBlank()
        }

        FormRow {
            FormNumberSelector(
                value = maxSets,
                label = "Max sets",
                range = 1..Int.MAX_VALUE,
                onUp = {
                    maxSets = it
                    winSets = max(winSets, ceil(maxSets.toDouble() / 2).roundToInt())
                },
                onDown = {
                    maxSets = it
                    winSets = min(maxSets, winSets)
                }
            )

            FormNumberSelector(
                value = winSets,
                label = "Win sets",
                range = 1..Int.MAX_VALUE,
                onUp = {
                    winSets = it
                    maxSets = max(maxSets, winSets)
                },
                onDown = {
                    winSets = it
                    maxSets = min(maxSets, winSets * 2)
                }
            )
        }

        FormRow {
            FormNumberSelector(
                value = maxPoints,
                label = "Max points",
                range = 1..Int.MAX_VALUE,
                onUp = { maxPoints = it },
                onDown = {
                    maxPoints = it
                    winPoints = min(winPoints, maxPoints)
                    pointsDifference = min(pointsDifference, winPoints)
                }
            )

            FormNumberSelector(
                value = winPoints,
                label = "Win points",
                range = 1..Int.MAX_VALUE,
                onUp = {
                    winPoints = it
                    maxPoints = max(maxPoints, winPoints)
                },
                onDown = {
                    winPoints = it
                    pointsDifference = min(pointsDifference, winPoints)
                }
            )

            FormNumberSelector(
                value = pointsDifference,
                label = "Points difference",
                range = 0..Int.MAX_VALUE,
                onUp = {
                    pointsDifference = it
                    winPoints = max(winPoints, pointsDifference)
                    maxPoints = max(maxPoints, winPoints)
                },
                onDown = { pointsDifference = it }
            )
        }

        FormRow {
            FormNumberSelector(
                value = gamePointsForWin,
                label = "Game points for win",
                range = 0..Int.MAX_VALUE,
                onUp = { gamePointsForWin = it },
                onDown = { gamePointsForWin = it }
            )

            FormNumberSelector(
                value = gamePointsForLose,
                label = "Game points for lose",
                range = 0..Int.MAX_VALUE,
                onUp = { gamePointsForLose = it },
                onDown = { gamePointsForLose = it }
            )

            FormNumberSelector(
                value = gamePointsForDraw,
                label = "Game points for draw",
                range = 0..Int.MAX_VALUE,
                onUp = { gamePointsForDraw = it },
                onDown = { gamePointsForDraw = it }
            )
        }

        FormRow {
            FormNumberSelector(
                value = gamePointsForRest,
                label = "Game points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = { gamePointsForRest = it },
                onDown = { gamePointsForRest = it }
            )

            FormNumberSelector(
                value = setPointsForRest,
                label = "Set points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = { setPointsForRest = it },
                onDown = { setPointsForRest = it }
            )

            FormNumberSelector(
                value = pointPointsForRest,
                label = "Point points for rest",
                range = 0..Int.MAX_VALUE,
                onUp = { pointPointsForRest = it },
                onDown = { pointPointsForRest = it }
            )
        }
    }
}