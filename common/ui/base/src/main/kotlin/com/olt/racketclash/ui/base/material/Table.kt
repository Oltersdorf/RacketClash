package com.olt.racketclash.ui.base.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private sealed class TableSortState {
    data object Up : TableSortState()
    data object Down : TableSortState()
    data object None : TableSortState()
    data object NotSortable : TableSortState()
}

sealed class TableSortDirection {
    data object Ascending : TableSortDirection()
    data object Descending : TableSortDirection()
}

sealed class LazyTableColumn<T>(
    val name: String,
    val weight: Float,
    val headerTextAlign: TextAlign?,
    val onSort: ((TableSortDirection) -> Unit)?
) {
    class Builder<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        onSort: ((TableSortDirection) -> Unit)? = null,
        val content: @Composable RowScope.(item: T, weight: Float) -> Unit
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign, onSort = onSort)

    class Text<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        onSort: ((TableSortDirection) -> Unit)? = null,
        val text: (T) -> String
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign, onSort = onSort)

    class Checkbox<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        onSort: ((TableSortDirection) -> Unit)? = null,
        val checked: (T) -> Boolean,
        val onCheckChanged: (T, Boolean) -> Unit
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign, onSort = onSort)

    class IconButton<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        val onClick: (T) -> Unit,
        val enabled: (T) -> Boolean = { true },
        val imageVector: ImageVector,
        val contentDescription: String
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign, onSort = null)

    class Link<T>(
        name: String = "",
        weight: Float = 1.0f,
        headerTextAlign: TextAlign? = null,
        onSort: ((TableSortDirection) -> Unit)? = null,
        val text: (T) -> String,
        val onClick: (T) -> Unit
    ) : LazyTableColumn<T>(name = name, weight = weight, headerTextAlign = headerTextAlign, onSort = onSort)
}

@Composable
fun <T> Table(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemsSpacedBy: Dp = 0.dp,
    showHeader: Boolean = true,
    onClick: ((T) -> Unit)? = null,
    columns: List<LazyTableColumn<T>>,
    drawDividers: Boolean = true,
    isLoading: Boolean
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemsSpacedBy)
    ) {
        if (showHeader)
            Header(columns = columns)

        Body(items = items, columns = columns, onClick = onClick, drawDividers = drawDividers, isLoading = isLoading)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> Header(columns: List<LazyTableColumn<T>>) {
    Surface(tonalElevation = 5.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp, horizontal = 10.dp)) {
            var sortStateColumns by remember { mutableStateOf(
                columns.map {
                    if (it.onSort == null)
                        TableSortState.NotSortable to it
                    else
                        TableSortState.None to it
                }
            ) }
            var currentSortedColumnIndex by remember { mutableStateOf<Int?>(null) }

            sortStateColumns.forEachIndexed { index, (sortState, column) ->
                Box(modifier = Modifier
                    .weight(column.weight)
                    .run {
                        when (sortState) {
                            TableSortState.Down -> {
                                pointerHoverIcon(PointerIcon.Hand)
                                    .clickable {
                                        column.onSort?.let { it(TableSortDirection.Ascending) }
                                        sortStateColumns = sortStateColumns.toMutableList().apply {
                                            this[index] = TableSortState.Up to column
                                        }
                                    }
                            }
                            TableSortState.Up -> {
                                pointerHoverIcon(PointerIcon.Hand)
                                    .clickable {
                                        column.onSort?.let { it(TableSortDirection.Descending) }
                                        sortStateColumns = sortStateColumns.toMutableList().apply {
                                            this[index] = TableSortState.Down to column
                                        }
                                    }
                            }
                            TableSortState.None -> {
                                pointerHoverIcon(PointerIcon.Hand)
                                    .clickable {
                                        column.onSort?.let { it(TableSortDirection.Descending) }
                                        sortStateColumns = sortStateColumns.toMutableList().apply {
                                            this[index] = TableSortState.Down to column

                                            val lastSortedColumnIndex = currentSortedColumnIndex
                                            if (lastSortedColumnIndex != null)
                                                this[lastSortedColumnIndex] =
                                                    TableSortState.None as TableSortState to this[lastSortedColumnIndex].second
                                        }

                                        currentSortedColumnIndex = index
                                    }
                            }
                            TableSortState.NotSortable -> this
                        }
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        when (sortState) {
                            is TableSortState.Down -> ExposedDropdownMenuDefaults.TrailingIcon(expanded = true)
                            is TableSortState.None -> {}
                            is TableSortState.Up -> ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                            TableSortState.NotSortable -> {}
                        }

                        Text(
                            text = column.name,
                            textAlign = column.headerTextAlign
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> Body(
    items: List<T>,
    columns: List<LazyTableColumn<T>>,
    onClick: ((T) -> Unit)?,
    drawDividers: Boolean,
    isLoading: Boolean
) {
    if (isLoading)
        Loading(paddingValues = PaddingValues(10.dp))
    else
        items.forEachIndexed { index, item ->
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
                        is LazyTableColumn.Link<T> -> TableLink(item = item, column = it)
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
    Box(
        modifier = Modifier.weight(column.weight),
        contentAlignment = Alignment.Center
    ) {
        SimpleIconButton(
            enabled = column.enabled(item),
            imageVector = column.imageVector,
            contentDescription = column.contentDescription,
            onClick = { column.onClick(item) }
        )
    }
}

@Composable
private fun <T> RowScope.TableLink(
    item: T,
    column: LazyTableColumn.Link<T>
) {
    Link(
        modifier = Modifier.weight(weight = column.weight),
        text = column.text(item),
        onClick = { column.onClick(item) }
    )
}