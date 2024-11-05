package com.olt.racketclash.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.theme.AdditionalMaterialTheme

@Composable
fun Status(
    modifier: Modifier = Modifier,
    isOkay: Boolean
) {
    Box(
        modifier = modifier
    ) {
        Box(modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(color = if (isOkay) AdditionalMaterialTheme.current.won else AdditionalMaterialTheme.current.lost)
        )
    }

}