package com.olt.racketclash.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.navigation.Screens

@Composable
fun Loading(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.padding(paddingValues = paddingValues).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnWithScroll(
    modifier: Modifier = Modifier,
    itemsSpacedBy: Dp = 0.dp,
    header: @Composable (LazyItemScope.() -> Unit)? = null,
    content: LazyListScope.() -> Unit
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
            if (header != null) {
                stickyHeader(content = header)
            }

            content()
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = scrollState),
            style = LocalScrollbarStyle.current.copy(hoverColor = MaterialTheme.colorScheme.secondary, unhoverColor = MaterialTheme.colorScheme.secondary)
        )
    }
}

sealed class TournamentTabs(val tab: Tab) {
    data object Teams : TournamentTabs(tab = Tab(target = Screens.Teams, imageVector = Icons.Default.Person, text = "Teams"))
    data object Players : TournamentTabs(tab = Tab(target = Screens.Players, imageVector = Icons.Default.Person, text = "Player"))
    data object Games : TournamentTabs(tab = Tab(target = Screens.Rounds ,imageVector = Icons.Default.List, text = "Games"))
}

data class Tab(
    val target: Screens,
    val imageVector: ImageVector,
    val text: String
)

@Composable
fun TournamentScaffold(
    topAppBarTitle: String,
    topAppBarActions: @Composable (RowScope.() -> Unit) = {},
    hasBackPress: Boolean = false,
    selectedTab: TournamentTabs?,
    navigateTo: (Screens, Navigator) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {

    NavigationScaffold(
        topAppBarTitle = topAppBarTitle,
        topAppBarActions = topAppBarActions,
        hasBackPress = hasBackPress,
        tabs = listOf(TournamentTabs.Teams.tab, TournamentTabs.Players.tab, TournamentTabs.Games.tab),
        selectedTab = selectedTab?.tab,
        navigateTo = navigateTo,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationScaffold(
    topAppBarTitle: String,
    topAppBarActions: @Composable (RowScope.() -> Unit) = {},
    hasBackPress: Boolean = false,
    tabs: List<Tab>,
    selectedTab: Tab?,
    navigateTo: (Screens, Navigator) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = topAppBarTitle) },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = topAppBarActions,
                navigationIcon = {
                    val navigator = LocalNavigator.currentOrThrow

                    if (hasBackPress) {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    tabs.forEach {
                        NavigationScaffoldButton(
                            tab = it,
                            selected = it == selectedTab,
                            navigateTo = navigateTo
                        )
                    }
                }
            )
        },
        content = content
    )
}

@Composable
private fun RowScope.NavigationScaffoldButton(
    tab: Tab,
    selected: Boolean,
    navigateTo: (Screens, Navigator) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Button(
        onClick = { navigateTo(tab.target, navigator) },
        enabled = !selected,
        modifier = Modifier.weight(1.0f),
        shape = RectangleShape
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = tab.imageVector,
                contentDescription = tab.text
            )
            Text(text = tab.text)
        }
    }
}