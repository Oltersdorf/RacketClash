package com.olt.racketclash.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.LazyTableWithScroll
import com.olt.racketclash.ui.base.material.PageSelector

@Composable
fun <Item, Filter> FilteredLazyTable(
    state: ListState<Item, Filter, *>,
    columns: List<LazyTableColumn<Item>>,
    onPageClicked: (Int) -> Unit,
    filter: @Composable (Filter) -> Unit
) {
    Column {
        filter(state.filter)

        Surface(
            modifier = Modifier.weight(1.0f, fill = false),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shadowElevation = 1.dp
        ) {
            LazyTableWithScroll(
                items = state.items,
                isLoading = state.isLoading,
                columns = columns
            )
        }

        PageSelector(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            currentPage = state.currentPage,
            lastPage = state.lastPage,
            onPageClicked = onPageClicked
        )
    }
}