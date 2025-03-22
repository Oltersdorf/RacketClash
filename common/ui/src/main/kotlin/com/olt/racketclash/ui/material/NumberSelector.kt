package com.olt.racketclash.ui.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Composable
fun NumberSelector(
    modifier: Modifier,
    value: Int,
    label: String = "",
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
    steps: Int = 1,
    onUp: (Int) -> Unit,
    onDown: (Int) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value.toString(),
        onValueChange = {},
        singleLine = true,
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            Column {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "+$steps",
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clickable {
                            val newValue = value + steps
                            if (newValue > value && range.contains(newValue)) //Check for overflow and range
                                onUp(newValue)
                        }
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "-$steps",
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clickable {
                            val newValue = value - steps
                            if (newValue < value && range.contains(newValue)) //Check for underflow and range
                                onDown(newValue)
                        }
                )
            }
        }
    )
}