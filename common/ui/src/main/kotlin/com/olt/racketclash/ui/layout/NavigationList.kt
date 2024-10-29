package com.olt.racketclash.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun NavigationList(
    navList: List<Screens>,
    onClick: (Screens) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 50.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        navList.forEach {
            Card(
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand)
                    .fillMaxWidth(0.8f)
                    .requiredWidthIn(min = 400.dp)
                    .requiredHeight(100.dp),
                onClick = { onClick(it) },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = it.name, fontSize = MaterialTheme.typography.headlineLarge.fontSize)
                }
            }
        }
    }
}