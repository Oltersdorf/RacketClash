package com.olt.racketclash.ui.base.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.olt.racketclash.ui.base.material.*

@Composable
fun Form(
    title: String,
    isLoading: Boolean = false,
    abortButton: @Composable RowScope.() -> Unit = {},
    confirmButton: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            if (isLoading)
                Loading()
            else {
                content()

                Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
                    abortButton()
                    confirmButton()
                }
            }
        }
    }
}

@Composable
fun RowScope.FormButton(
    text: String,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .weight(1.0f)
            .requiredHeight(OutlinedTextFieldDefaults.MinHeight),
        shape = RectangleShape,
        enabled = enabled,
        onClick = onClick
    ) {
        icon?.let { Icon(imageVector = it, contentDescription = contentDescription) }
        Text(text = text)
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
    enabled: Boolean = true,
    isError: Boolean = false,
    label: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        singleLine = true,
        isError = isError,
        label = { Text(label) }
    )
}

@Composable
fun RowScope.FormTextField(
    value: String,
    enabled: Boolean = true,
    isError: Boolean = false,
    label: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.weight(0.5f),
        value = value,
        enabled = enabled,
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
    enabled: Boolean = true,
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
    steps: Int = 1,
    onUp: (Int) -> Unit,
    onDown: (Int) -> Unit
) {
    NumberSelector(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = label,
        enabled = enabled,
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
    enabled: Boolean = true,
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE,
    steps: Int = 1,
    onUp: (Int) -> Unit,
    onDown: (Int) -> Unit
) {
    NumberSelector(
        modifier = Modifier.weight(0.5f),
        value = value,
        label = label,
        enabled = enabled,
        range = range,
        steps = steps,
        onUp = onUp,
        onDown = onDown
    )
}

@Composable
fun RowScope.FormNumberSelector(
    value: Long,
    label: String = "",
    enabled: Boolean = true,
    range: LongRange = Long.MIN_VALUE..Long.MAX_VALUE,
    steps: Int = 1,
    onUp: (Long) -> Unit,
    onDown: (Long) -> Unit
) {
    NumberSelector(
        modifier = Modifier.weight(0.5f),
        value = value,
        label = label,
        enabled = enabled,
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
    enabled: Boolean = true,
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
        enabled = enabled,
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
    enabled: Boolean = true,
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
        enabled = enabled,
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
    enabled: Boolean = true,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.weight(0.5f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckChanged
        )
        Text(text = text)
    }
}

@Composable
fun FormButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        onClick = onClick,
        shape = RectangleShape
    ) {
        Text(text)
    }
}

@Composable
fun <T> FormTable(
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

        Table(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLowest).requiredHeightIn(max = 400.dp),
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