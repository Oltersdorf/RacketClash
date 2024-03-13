package com.olt.racketclash

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.olt.racketclash.navigation.RootNavigator
import com.olt.racketclash.theme.DarkColors
import com.olt.racketclash.ui.RacketClashNavigator

fun main() {
    val rootNavigator = RootNavigator()

    application {
        Window(
            title = "Racket Clash",
            onCloseRequest = ::exitApplication
        ) {
            MaterialTheme(
                colorScheme = DarkColors
            ) {
                RacketClashNavigator(rootNavigator = rootNavigator)
            }
        }
    }
}
