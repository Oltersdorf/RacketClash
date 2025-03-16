package com.olt.racketclash.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.component.SimpleIconButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import racketclash.common.ui.generated.resources.*
import racketclash.common.ui.generated.resources.Res
import racketclash.common.ui.generated.resources.dark_mode
import racketclash.common.ui.generated.resources.light_mode

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
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            Navigator(drawerState = drawerState, database = database)
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