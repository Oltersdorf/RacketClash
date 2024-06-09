package com.olt.racketclash

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.olt.racketclash.theme.DarkColors
import com.olt.racketclash.app.RacketClashNavigator

fun main() {
    application {
        Window(
            title = "Racket Clash",
            onCloseRequest = ::exitApplication
        ) {
            MaterialTheme(
                colorScheme = DarkColors
            ) {
                RacketClashNavigator()
            }
        }
    }
}
