package com.olt.racketclash.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.component.Link
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun TextNavigationList(
    title: String,
    navList: List<Screens>,
    onClick: (Screens) -> Unit
) {
    NavigationList(
        navList = navList,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = title,
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )
    }
}

@Composable
internal fun LinkNavigationList(
    title: String,
    navList: List<Screens>,
    onLinkClick: () -> Unit,
    onClick: (Screens) -> Unit
) {
    NavigationList(
        navList = navList,
        onClick = onClick
    ) {
        Link(
            modifier = Modifier.padding(20.dp),
            text = title,
            onClick = onLinkClick,
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )
    }
}

@Composable
private fun NavigationList(
    navList: List<Screens>,
    onClick: (Screens) -> Unit,
    title: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 50.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(0.5f), thickness = 1.dp)
            title()
            HorizontalDivider(modifier = Modifier.weight(0.5f), thickness = 1.dp)
        }

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