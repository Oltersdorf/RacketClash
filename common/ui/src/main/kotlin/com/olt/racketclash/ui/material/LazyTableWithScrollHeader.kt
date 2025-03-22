package com.olt.racketclash.ui.material

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.LazyTableWithScrollHeader(
    title: String,
    onAddClicked: (() -> Unit)?
) {
    Text(
        text = title,
        modifier = Modifier.weight(1.0f).padding(start = 10.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.titleLarge
    )
    SimpleIconButton(
        modifier = Modifier.padding(top = 5.dp, end = 5.dp, bottom = 5.dp),
        imageVector = Icons.Default.Add,
        contentDescription = "Add",
        enabled = onAddClicked != null,
        onClick = onAddClicked ?: {}
    )
}