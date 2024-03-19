package com.olt.racketclash.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class LazyTableColumn<T>(
    val name: String = "",
    val weight: Float = 1.0f,
    val textAlign: TextAlign? = null,
    val content: @Composable RowScope.(item: T, weight: Float) -> Unit
)

@Composable
fun <T> LazyTableWithScroll(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemsSpacedBy: Dp = 0.dp,
    showHeader: Boolean = true,
    onClick: ((T) -> Unit)? = null,
    columns: List<LazyTableColumn<T>>
) {
    Box(
        modifier = modifier
    ) {
        val scrollState = rememberLazyListState()
        val canScroll = scrollState.canScrollBackward || scrollState.canScrollForward

        LazyColumn(
            modifier = Modifier.padding(end = if (canScroll) 14.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(itemsSpacedBy),
            state = scrollState
        ) {
            if (showHeader)
                header(columns = columns)

            body(items = items, columns = columns, onClick = onClick)
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = scrollState),
            style = LocalScrollbarStyle.current.copy(hoverColor = MaterialTheme.colorScheme.secondary, unhoverColor = MaterialTheme.colorScheme.secondary)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.header(columns: List<LazyTableColumn<*>>) {
    stickyHeader {
        Surface(tonalElevation = 1.dp) {
            Row(modifier = Modifier.fillMaxWidth()) {
                columns.forEach {
                    Text(
                        text = it.name,
                        modifier = Modifier.weight(it.weight)
                    )
                }
            }
        }
    }
}

private fun <T> LazyListScope.body(
    items: List<T>,
    columns: List<LazyTableColumn<T>>,
    onClick: ((T) -> Unit)?
) {
    items(items = items) { item ->
        Row(
            modifier = Modifier.clickable { onClick?.let { it(item) } },
            verticalAlignment = Alignment.CenterVertically
        ) {
            columns.forEach {
                it.content(this, item, it.weight)
            }
        }
    }
}