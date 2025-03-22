package com.olt.racketclash.ui.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

@Composable
fun PageSelector(
    modifier: Modifier = Modifier,
    currentPage: Int,
    lastPage: Int,
    onPageClicked: (Int) -> Unit
) {
    // 1 <= currentPage <= lastPage
    require(1 <= currentPage)
    require(currentPage <= lastPage)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        SimpleIconButton(
            enabled = currentPage != 1,
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "Backward"
        ) { onPageClicked(currentPage - 1) }

        Pages(
            currentPage = currentPage,
            lastPage = lastPage,
            onPageClicked = onPageClicked
        )

        SimpleIconButton(
            enabled = currentPage != lastPage,
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Forward"
        ) { onPageClicked(currentPage + 1) }
    }
}

@Composable
private fun Pages(
    currentPage: Int,
    lastPage: Int,
    onPageClicked: (Int) -> Unit
) {
    if (currentPage != 1)
        PageLink(page = 1, onClick = onPageClicked)
    else
        PageText(text = "1")

    // ... c-3 c-2 c-1 c
    if (1 < currentPage - 4)
        PageText(text = "...")

    val pagesInBetween = max(currentPage - 3, 2)..min(currentPage + 3, lastPage - 1)
    for (page in pagesInBetween) {
        if (page == currentPage)
            PageText(text = "$page")
        else
            PageLink(page = page, onClick = onPageClicked)
    }

    // c c+1 c+2 c+3 ...
    if (currentPage + 4 < lastPage)
        PageText(text = "...")

    if (lastPage != 1) {
        if (currentPage == lastPage)
            PageText(text = "$lastPage")
        else
            PageLink(page = lastPage, onClick = onPageClicked)
    }
}

@Composable
private fun PageText(text: String) =
    Text(text, style = MaterialTheme.typography.titleLarge)

@Composable
private fun PageLink(
    page: Int,
    onClick: (Int) -> Unit
) = Link(
    text = "$page",
    style = MaterialTheme.typography.titleLarge
) { onClick(page) }