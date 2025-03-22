package com.olt.racketclash.ui.material

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme

@Composable
fun RatioBar(
    modifier: Modifier = Modifier,
    left: Int = 0,
    middle: Int = 0,
    right: Int = 0
) {
    val sum: Float = (left + middle + right).toFloat()

    Column(modifier = modifier) {
        Row(modifier = Modifier.border(width = 1.dp, color = MaterialTheme.colorScheme.outline)) {
            if (left >= 1)
                Box(modifier = Modifier
                    .height(20.dp)
                    .weight(left / sum)
                    .background(AdditionalMaterialTheme.current.won)
                )
            if (middle >= 1)
                Box(modifier = Modifier
                    .height(20.dp)
                    .weight(middle / sum)
                    .background(AdditionalMaterialTheme.current.neutral)
                )
            if (right >= 1)
                Box(modifier = Modifier
                    .height(20.dp)
                    .weight(right / sum)
                    .background(AdditionalMaterialTheme.current.lost)
                )
        }

        Row {
            if (left >= 1)
                Text(
                    left.toString(),
                    modifier = Modifier.weight(left / sum).requiredWidth(IntrinsicSize.Min),
                    textAlign = TextAlign.Center,
                    color = AdditionalMaterialTheme.current.won,
                    fontWeight = FontWeight.Bold
                )
            if (middle >= 1)
                Text(
                    middle.toString(),
                    modifier = Modifier.weight(middle / sum).requiredWidth(IntrinsicSize.Min),
                    textAlign = TextAlign.Center,
                    color = AdditionalMaterialTheme.current.neutral,
                    fontWeight = FontWeight.Bold
                )
            if (right >= 1)
                Text(
                    right.toString(),
                    modifier = Modifier.weight(right / sum).requiredWidth(IntrinsicSize.Min),
                    textAlign = TextAlign.Center,
                    color = AdditionalMaterialTheme.current.lost,
                    fontWeight = FontWeight.Bold
                )
        }
    }
}