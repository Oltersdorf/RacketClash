package com.olt.racketclash.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class LazyTableColumn<T>(
    val name: String,
    val weight: Float,
    val headerTextAlign: TextAlign?
) {
    class Builder<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        val content: @Composable RowScope.(item: T, weight: Float) -> Unit
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign)

    class Text<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        val text: (T) -> String
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign)

    class Checkbox<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        val checked: (T) -> Boolean,
        val onCheckChanged: (T, Boolean) -> Unit
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign)

    class IconButton<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        val onClick: (T) -> Unit,
        val enabled: (T) -> Boolean = { true },
        val imageVector: ImageVector,
        val contentDescription: String
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign)
}

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
private fun <T> LazyListScope.header(columns: List<LazyTableColumn<T>>) {
    stickyHeader {
        Surface(tonalElevation = 1.dp) {
            Row(modifier = Modifier.fillMaxWidth()) {
                columns.forEach {
                    Text(
                        text = it.name,
                        modifier = Modifier.weight(it.weight),
                        textAlign = it.headerTextAlign
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
                when (it) {
                    is LazyTableColumn.Builder<T> -> it.content(this, item, it.weight)
                    is LazyTableColumn.Text<T> -> TableText(item = item, column = it)
                    is LazyTableColumn.Checkbox<T> -> TableCheckbox(item = item, column = it)
                    is LazyTableColumn.IconButton<T> -> TableIconButton(item = item, column = it)
                }
            }
        }
    }
}

@Composable
private fun <T> RowScope.TableText(
    item: T,
    column: LazyTableColumn.Text<T>
) {
    Text(
        text = column.text(item),
        modifier = Modifier.weight(column.weight)
    )
}

@Composable
private fun <T> RowScope.TableCheckbox(
    item: T,
    column: LazyTableColumn.Checkbox<T>
) {
    Checkbox(
        checked = column.checked(item),
        onCheckedChange = { newChecked -> column.onCheckChanged(item, newChecked) },
        modifier = Modifier.weight(column.weight)
    )
}

@Composable
private fun <T> RowScope.TableIconButton(
    item: T,
    column: LazyTableColumn.IconButton<T>
) {
    IconButton(
        modifier = Modifier.weight(column.weight),
        onClick = { column.onClick(item) },
        enabled = column.enabled(item)
    ) {
        Icon(
            imageVector = column.imageVector,
            contentDescription = column.contentDescription
        )
    }
}