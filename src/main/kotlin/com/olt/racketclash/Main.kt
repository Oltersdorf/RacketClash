package com.olt.racketclash

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.olt.racketclash.theme.DarkColors
import com.olt.racketclash.app.RacketClashNavigator
import com.olt.racketclash.data.database.Database
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
                RacketClashNavigator(database = database)
            }
        }
    }
}
