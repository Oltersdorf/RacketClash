package com.olt.racketclash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.olt.racketclash.database.api.Database
import com.olt.racketclash.ui.base.material.SimpleIconButton
import com.olt.racketclash.ui.view.AddSchedule
import com.olt.racketclash.ui.view.Categories
import com.olt.racketclash.ui.view.Category
import com.olt.racketclash.ui.view.Player
import com.olt.racketclash.ui.view.Players
import com.olt.racketclash.ui.view.Rule
import com.olt.racketclash.ui.view.Rules
import com.olt.racketclash.ui.view.Schedule
import com.olt.racketclash.ui.view.Start
import com.olt.racketclash.ui.view.Team
import com.olt.racketclash.ui.view.Teams
import com.olt.racketclash.ui.view.Tournament
import com.olt.racketclash.ui.view.Tournaments
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.view.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacketClashUI(
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit,
    isDarkMode: Boolean,
    onSwitchDarkMode: () -> Unit,
    database: Database,
    racketClashTopBar: @Composable (@Composable () -> Unit) -> Unit
) {
    var viewHistory by remember { mutableStateOf<List<View>>(listOf(View.Start)) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            racketClashTopBar {
                CenterAlignedTopAppBar(
                    title = { Text("Racket Clash") },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    navigationIcon = {
                        Row {
                            SimpleIconButton(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                enabled = viewHistory.size >= 2
                            ) { viewHistory = viewHistory.dropLast(1) }

                            SimpleIconButton(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            ) {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        }
                    },
                    actions = {
                        TopAppBarActions(
                            onMinimize = onMinimize,
                            onMaximize = onMaximize,
                            onClose = onClose,
                            isDarkMode = isDarkMode,
                            onSwitchDarkMode = onSwitchDarkMode
                        )
                    }
                )
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NavigationDrawer(
                drawerState = drawerState,
                currentView = viewHistory.last(),
                setViewHistory = { viewHistory = listOf(it) }
            ) {
                when (val cv = viewHistory.last()) {
                    View.Start -> Start(database = database) { viewHistory += it }
                    is View.AddSchedule -> AddSchedule(database = database, categoryId = cv.categoryId, categoryName = cv.categoryName, tournamentId = cv.tournamentId) { viewHistory += View.Schedule(tournamentId = cv.tournamentId) }
                    is View.Categories -> Categories(database = database, tournamentId = cv.tournamentId) { viewHistory += it }
                    is View.Category -> Category(database = database, categoryId = cv.categoryId) { viewHistory += it }
                    is View.Rule -> Rule(database = database, ruleId = cv.id) { viewHistory += it }
                    View.Rules -> Rules(database = database) { viewHistory += it }
                    is View.Player -> Player(database = database, playerId = cv.playerId) { viewHistory += it }
                    View.Players -> Players(database = database) { viewHistory += it }
                    is View.Schedule -> Schedule(database = database, tournamentId = cv.tournamentId)
                    is View.Team -> Team(database = database, teamId = cv.teamId) { viewHistory += it }
                    is View.Teams -> Teams(database = database, tournamentId = cv.tournamentId) { viewHistory += it }
                    is View.Tournament -> Tournament(database = database, tournamentId = cv.tournamentId) { viewHistory += it }
                    View.Tournaments -> Tournaments(database = database) { viewHistory += it }
                }
            }
        }
    }
}

@Composable
private fun TopAppBarActions(
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit,
    isDarkMode: Boolean,
    onSwitchDarkMode: () -> Unit
) {
    SimpleIconButton(
        painter = if (isDarkMode)
            painterResource(Res.drawable.light_mode)
        else
            painterResource(Res.drawable.dark_mode),
        contentDescription = "Light/Dark mode",
        onClick = onSwitchDarkMode
    )

    VerticalDivider(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
        thickness = 2.dp
    )

    SimpleIconButton(
        painter = painterResource(Res.drawable.minimize),
        contentDescription = "Minimize",
        onClick = onMinimize
    )

    SimpleIconButton(
        painter = painterResource(Res.drawable.maximize),
        contentDescription = "Maximize",
        onClick = onMaximize
    )

    SimpleIconButton(
        painter = painterResource(Res.drawable.close),
        contentDescription = "Close",
        onClick = onClose
    )
}

@Composable
private fun NavigationDrawer(
    drawerState: DrawerState,
    currentView: View,
    setViewHistory: (View) -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        content = content,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape
            ) {
                Text(
                    text = "Racket Clash",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Start") },
                    selected = currentView is View.Start,
                    onClick = { setViewHistory(View.Start) }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Tournaments") },
                    selected = currentView is View.Tournaments,
                    onClick = { setViewHistory(View.Tournaments) }
                )
                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Players") },
                    selected = currentView is View.Players,
                    onClick = { setViewHistory(View.Players) }
                )
                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Rules") },
                    selected = currentView is View.Rules,
                    onClick = { setViewHistory(View.Rules) }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { }
                )

                NavigationDrawerItem(
                    shape = RectangleShape,
                    label = { Text("About") },
                    selected = false,
                    onClick = { }
                )
            }
        }
    )
}