package com.olt.racketclash.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.olt.racketclash.state.list.ListState
import com.olt.racketclash.ui.base.material.LazyTableColumn
import com.olt.racketclash.ui.base.material.PageSelector
import com.olt.racketclash.ui.base.material.Table

@Composable
fun <Item, Filter> FilteredTable(
    state: ListState<Item, Filter, *>,
    columns: List<LazyTableColumn<Item>>,
    onPageClicked: (Int) -> Unit,
    filter: @Composable (Filter) -> Unit
) {
    Column {
        filter(state.filter)

        Table(
            items = state.items,
            isLoading = state.isLoading,
            columns = columns
        )

        PageSelector(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            currentPage = state.currentPage,
            lastPage = state.lastPage,
            onPageClicked = onPageClicked
        )
    }
}