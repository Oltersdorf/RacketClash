package com.olt.racketclash.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.language.translations.Language
import com.olt.racketclash.navigation.Screens

sealed class TournamentTabs(val tab: Tab) {
    data class Teams(val language: Language) : TournamentTabs(
        tab = Tab(target = Screens.Teams(language = language),
            imageVector = Icons.Default.Person,
            text = language.teams)
    )
    data class Players(val language: Language) : TournamentTabs(
        tab = Tab(target = Screens.Players(language = language),
            imageVector = Icons.Default.Person,
            text = language.players)
    )
    data class Games(val language: Language) : TournamentTabs(
        tab = Tab(target = Screens.Games(language = language) ,
            imageVector = Icons.Default.List,
            text = language.games)
    )
}

data class Tab(
    val target: Screens,
    val imageVector: ImageVector,
    val text: String
)

@Composable
fun TournamentScaffold(
    language: Language,
    topAppBarTitle: String,
    topAppBarActions: @Composable (RowScope.() -> Unit) = {},
    hasBackPress: Boolean = false,
    selectedTab: TournamentTabs?,
    navigateTo: (Screens, Navigator) -> Unit,
    content: @Composable () -> Unit
) {

    NavigationScaffold(
        topAppBarTitle = topAppBarTitle,
        topAppBarActions = topAppBarActions,
        hasBackPress = hasBackPress,
        tabs = listOf(
            TournamentTabs.Teams(language = language).tab,
            TournamentTabs.Players(language = language).tab,
            TournamentTabs.Games(language = language).tab
        ),
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
    content: @Composable () -> Unit
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

                    if (hasBackPress)
                        BackButton { navigator.pop() }
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
        content = {
            Surface(modifier = Modifier.fillMaxSize().padding(paddingValues = it)) {
                content()
            }
        }
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