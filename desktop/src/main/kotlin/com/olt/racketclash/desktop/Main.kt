package com.olt.racketclash.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.navigate.Navigator
import com.olt.racketclash.ui.theme.RacketClashMaterialTheme
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories

fun main() {
    val racketClashPath = Path(System.getProperty("user.home"), ".racketClash")
    racketClashPath.createDirectories()

    val database = Database(path = racketClashPath.absolutePathString())

    application {
        Window(
            title = "Racket Clash",
            onCloseRequest = ::exitApplication
        ) {
            RacketClashMaterialTheme { isDarkMode, switchDarkMode ->
                Navigator(isDarkMode = isDarkMode, switchDarkMode = switchDarkMode, database = database)
            }
        }
    }
}