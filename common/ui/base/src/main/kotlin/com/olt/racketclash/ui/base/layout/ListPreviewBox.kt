package com.olt.racketclash.ui.base.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.base.material.Link
import com.olt.racketclash.ui.base.material.Loading

@Composable
fun <T> ListPreviewBox(
    name: String,
    isLoading: Boolean,
    items: List<T>,
    onNavigateMore: () -> Unit,
    itemBuilder: @Composable (T) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 1.dp
    ) {
        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = name,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Box(modifier = Modifier.padding(10.dp)) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (isLoading)
                        Loading()
                    else {
                        if (items.isEmpty())
                            Text("No items available")
                        else
                            items.forEach { itemBuilder(it) }
                    }

                    Link(
                        text = "more >>",
                        modifier = Modifier.align(Alignment.End),
                        onClick = onNavigateMore
                    )
                }
            }
        }
    }
}

@Composable
fun ListPreviewBoxLink(
    text: String,
    subText: String,
    onClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Link(text = text, onClick = onClick)
        Text(text = subText, fontSize = MaterialTheme.typography.labelMedium.fontSize)
    }
}