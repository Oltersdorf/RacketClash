package com.olt.racketclash.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NumberSelector(
    modifier: Modifier = Modifier,
    label: String? = null,
    value: Int,
    valuePostText: String = "",
    onValueChange: (Int) -> Unit,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    step: Int = 1
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label != null) Text(label)

        ArrowLeftButton(enabled = value - step >= min) { onValueChange(value - step) }

        Text(value.toString() + valuePostText)

        ArrowRightButton(enabled = value + step <= max) { onValueChange(value + step) }
    }
}