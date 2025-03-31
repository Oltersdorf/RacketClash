package com.olt.racketclash.ui.base.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun <Tab> TabRow(
    selectedTabIndex: Int,
    tabs: List<Tab>,
    onTabClick: (Tab) -> Unit,
    tabText: (Tab) -> String
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = {
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(it[selectedTabIndex])
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
            ) {}
        },
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .width(200.dp * tabs.size)
            .height(40.dp),
        divider = {},
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabClick(tab) },
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .zIndex(2f)
                    .clip(RoundedCornerShape(20.dp))
                    .height(40.dp)
                    .pointerHoverIcon(icon = PointerIcon.Hand)
            ) {
                Text(tabText(tab))
            }
        }
    }
}