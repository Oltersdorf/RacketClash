package com.olt.racketclash.ui.navigate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.util.Link

@Composable
fun Navigator(
    navLinks: Map<String, Screens>,
    changeToScreen: (Screens) -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            navLinks.entries.forEachIndexed { index, entry ->
                Link(text = entry.key, fontSize = MaterialTheme.typography.titleLarge.fontSize) {
                    changeToScreen(entry.value)
                }

                if (index + 1 < navLinks.size)
                    Text(text = "/", modifier = Modifier.padding(horizontal = 5.dp))
            }
        }

        content()
    }
}