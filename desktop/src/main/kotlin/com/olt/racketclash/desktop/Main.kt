package com.olt.racketclash.desktop

import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.window.*
import com.olt.racketclash.database.RacketClashDatabaseImpl
import com.olt.racketclash.ui.RacketClashUI
import com.olt.racketclash.ui.theme.RacketClashMaterialTheme
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

fun main() {
    val racketClashPath = Path(System.getProperty("user.home"), ".racketClash")
    racketClashPath.createDirectories()

    val database = RacketClashDatabaseImpl()

    application {
        Window(
            undecorated = true,
            onCloseRequest = ::exitApplication
        ) {
            RacketClashMaterialTheme { isDarkMode, switchDarkMode ->
                val windowPlacement = remember { mutableStateOf(window.placement) }
                val oldFloatingWindowSize = remember { mutableStateOf(window.size) }
                val oldWindowFloatingLocation = remember { mutableStateOf(window.location) }

                RacketClashUI(
                    onMinimize = { window.isMinimized = true },
                    onMaximize = {
                        windowPlacement.value = if (windowPlacement.value == WindowPlacement.Floating) {
                            oldFloatingWindowSize.value = window.size
                            oldWindowFloatingLocation.value = window.location
                            val insets = Toolkit.getDefaultToolkit().getScreenInsets(window.graphicsConfiguration)
                            val bounds = window.graphicsConfiguration.bounds
                            window.location = Point(bounds.x, bounds.y)
                            window.size = Dimension(bounds.width, bounds.height - insets.bottom)
                            WindowPlacement.Maximized
                        } else {
                            window.size = oldFloatingWindowSize.value
                            window.location = oldWindowFloatingLocation.value
                            WindowPlacement.Floating
                        }
                    },
                    onClose = ::exitApplication,
                    isDarkMode = isDarkMode,
                    onSwitchDarkMode = switchDarkMode,
                    database = database,
                    racketClashTopBar = {
                        WindowDraggableArea(
                            modifier = Modifier.pointerHoverIcon(icon = PointerIcon(Cursor(Cursor.MOVE_CURSOR))),
                            content = it
                        )
                    }
                )
            }
        }
    }
}