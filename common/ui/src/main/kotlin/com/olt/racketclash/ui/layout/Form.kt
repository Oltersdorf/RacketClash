package com.olt.racketclash.ui.layout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.component.*

@Composable
fun Form(
    title: String,
    isLoading: Boolean = false,
    isSavable: Boolean = true,
    onSave: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth(0.6f)
        .background(color = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .align(Alignment.Center)
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.displayLarge.fontSize
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Box(modifier = Modifier.fillMaxWidth().padding(top = 50.dp)) {
                val verticalScrollState = rememberScrollState()
                val canScroll = verticalScrollState.canScrollBackward || verticalScrollState.canScrollForward

                Column(
                    modifier = Modifier
                        .verticalScroll(verticalScrollState)
                        .padding(end = if (canScroll) 14.dp else 0.dp),
                    verticalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    if (isLoading)
                        Loading()
                    else {
                        content()

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(OutlinedTextFieldDefaults.MinHeight),
                            shape = RectangleShape,
                            enabled = isSavable,
                            onClick = onSave
                        ) {
                            Text(text = "Save")
                        }
                    }
                }

                if (canScroll) {
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        adapter = rememberScrollbarAdapter(verticalScrollState),
                        style = LocalScrollbarStyle.current.copy(
                            hoverColor = MaterialTheme.colorScheme.primary,
                            unhoverColor = MaterialTheme.colorScheme.primary,
                            shape = RectangleShape
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FormRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun FormTextField(
    value: String,
    isError: Boolean = false,
    label: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        isError = isError,
        label = { Text(label) }
    )
}

@Composable
fun RowScope.FormTextField(
    value: String,
    isError: Boolean = false,
    label: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.weight(0.5f),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        isError = isError,
        label = { Text(label) }
    )
}

@Composable
fun FormNumberSelector(
    value: Int,
    label: String = "",
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
    steps: Int = 1,
    onUp: (Int) -> Unit,
    onDown: (Int) -> Unit
) {
    NumberSelector(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = label,
        range = range,
        steps = steps,
        onUp = onUp,
        onDown = onDown
    )
}

@Composable
fun RowScope.FormNumberSelector(
    value: Int,
    label: String = "",
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
    steps: Int = 1,
    onUp: (Int) -> Unit,
    onDown: (Int) -> Unit
) {
    NumberSelector(
        modifier = Modifier.weight(0.5f),
        value = value,
        label = label,
        range = range,
        steps = steps,
        onUp = onUp,
        onDown = onDown
    )
}

@Composable
fun <T> FormDropDownTextField(
    text: String,
    label: String = "",
    readOnly: Boolean = false,
    onTextChange: (String) -> Unit = {},
    dropDownItems: List<T>,
    dropDownItemText: @Composable (T) -> Unit,
    onItemClicked: (T) -> Unit
) {
    DropDownTextField(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        label = label,
        readOnly = readOnly,
        onTextChange = onTextChange,
        dropDownItems = dropDownItems,
        dropDownItemText = dropDownItemText,
        onItemClick = onItemClicked
    )
}

@Composable
fun <T> RowScope.FormDropDownTextField(
    text: String,
    label: String = "",
    readOnly: Boolean = false,
    onTextChange: (String) -> Unit = {},
    dropDownItems: List<T>,
    dropDownItemText: @Composable (T) -> Unit,
    onItemClicked: (T) -> Unit
) {
    DropDownTextField(
        modifier = Modifier.weight(0.5f),
        text = text,
        label = label,
        readOnly = readOnly,
        onTextChange = onTextChange,
        dropDownItems = dropDownItems,
        dropDownItemText = dropDownItemText,
        onItemClick = onItemClicked
    )
}

@Composable
fun RowScope.FormCheckBox(
    text: String,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.weight(0.5f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckChanged
        )
        Text(text = text)
    }
}

@Composable
fun FormButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RectangleShape
    ) {
        Text(text)
    }
}

@Composable
fun <T> FormTable(
    title: String? = null,
    onTitleAdd: (() -> Unit)? = null,
    items: List<T>,
    isLoading: Boolean = false,
    columns: List<LazyTableColumn<T>>,
    currentPage: Int,
    lastPage: Int,
    onPageClicked: (Int) -> Unit,
    searchBar: @Composable () -> Unit
) {
    Column {
        searchBar()

        LazyTableWithScroll(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLowest).requiredHeightIn(max = 400.dp),
            header = title?.let { { LazyTableWithScrollHeader(title = title, onAddClicked = onTitleAdd) } },
            items = items,
            isLoading = isLoading,
            columns = columns
        )

        if (lastPage != 1)
            PageSelector(
                currentPage = currentPage,
                lastPage = lastPage,
                onPageClicked = onPageClicked
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDateRangePicker(state: DateRangePickerState) {
    DateRangePicker(
        state = state,
        modifier = Modifier.fillMaxWidth().requiredHeightIn(max = 400.dp),
        colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    )
}