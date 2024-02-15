package com.olt.racketclash

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.olt.racketclash.theme.DarkColors
import com.olt.racketclash.ui.ProjectsScreen

fun main() {
    application {
        Window(
            title = "Racket Clash",
            onCloseRequest = ::exitApplication
        ) {
            MaterialTheme(
                colorScheme = DarkColors
            ) {
                Navigator(screen = ProjectsScreen()) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}
