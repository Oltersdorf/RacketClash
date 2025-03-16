package com.olt.racketclash.ui.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
internal fun RacketClashScaffold(
    header: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            shadowElevation = 5.dp,
            content = header
        )

        Box(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberScrollState()

            Column (
                modifier = Modifier.padding(50.dp).verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.spacedBy(50.dp),
                content = content
            )

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = scrollState),
                style = LocalScrollbarStyle.current.copy(
                    hoverColor = MaterialTheme.colorScheme.primary,
                    unhoverColor = MaterialTheme.colorScheme.primary,
                    shape = RectangleShape
                )
            )
        }
    }
}