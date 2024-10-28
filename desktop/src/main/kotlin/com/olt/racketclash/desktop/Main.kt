package com.olt.racketclash.desktop

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.theme.DarkColors
import com.olt.racketclash.ui.navigate.Navigator
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
            MaterialTheme(
                colorScheme = DarkColors
            ) {
                Navigator(database = database)
            }
        }
    }
}