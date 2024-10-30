package com.olt.racketclash.ui.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.component.SimpleIconButton

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
    header: @Composable (RowScope.() -> Unit)? = null,
    items: List<T>,
    itemsSpacedBy: Dp = 0.dp,
    showHeader: Boolean = true,
    onClick: ((T) -> Unit)? = null,
    columns: List<LazyTableColumn<T>>,
    drawDividers: Boolean = true
) {
    Column {
        if (header != null)
            Row(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
                verticalAlignment = Alignment.CenterVertically,
                content = header
            )

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

                body(items = items, columns = columns, onClick = onClick, drawDividers = drawDividers)
            }

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

@OptIn(ExperimentalFoundationApi::class)
private fun <T> LazyListScope.header(columns: List<LazyTableColumn<T>>) {
    stickyHeader {
        Surface(tonalElevation = 5.dp) {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp, horizontal = 10.dp)) {
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
    onClick: ((T) -> Unit)?,
    drawDividers: Boolean
) {
    itemsIndexed(items = items) { index, item ->
        Row(
            modifier = Modifier
                .run { if (onClick != null) clickable { onClick(item) } else this }
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .run { if (drawDividers && index != 0) bottomBorder(color = MaterialTheme.colorScheme.primary) else this },
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

private fun Modifier.bottomBorder(color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { 1.dp.toPx() }

        Modifier.drawBehind {
            val y = -density.run { 4.dp.toPx() }
            val x = -density.run { 10.dp.toPx() }

            drawLine(
                color = color,
                start = Offset(x = x, y = y),
                end = Offset(x = size.width + -x , y = y),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

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
    SimpleIconButton(
        modifier = Modifier.weight(column.weight),
        enabled = column.enabled(item),
        imageVector = column.imageVector,
        contentDescription = column.contentDescription,
        onClick = { column.onClick(item) }
    )
}