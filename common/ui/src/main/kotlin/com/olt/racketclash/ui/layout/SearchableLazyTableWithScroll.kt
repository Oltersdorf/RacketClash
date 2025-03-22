package com.olt.racketclash.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.olt.racketclash.ui.material.LazyTableWithScrollHeader
import com.olt.racketclash.ui.material.PageSelector

@Composable
fun <T> SearchableLazyTableWithScroll(
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        searchBar()

        LazyTableWithScroll(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainer).weight(1.0f, fill = false),
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