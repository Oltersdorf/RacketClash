package com.olt.racketclash.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsView(content: @Composable ColumnScope.() -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        val verticalScrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .requiredWidthIn(500.dp)
                .fillMaxWidth(0.5f)
                .padding(vertical = 50.dp)
                .verticalScroll(verticalScrollState),
            verticalArrangement = Arrangement.spacedBy(space = 50.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = verticalScrollState),
            style = LocalScrollbarStyle.current.copy(hoverColor = MaterialTheme.colorScheme.secondary, unhoverColor = MaterialTheme.colorScheme.secondary)
        )
    }
}