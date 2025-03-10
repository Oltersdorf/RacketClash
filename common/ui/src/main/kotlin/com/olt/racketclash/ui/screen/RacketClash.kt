package com.olt.racketclash.ui.screen

import androidx.compose.runtime.Composable
import com.olt.racketclash.ui.layout.TextNavigationList
import com.olt.racketclash.ui.Screens

@Composable
internal fun RacketClash(navigateTo: (Screens) -> Unit) {
    TextNavigationList(
        title = "Racket Clash",
        navList = listOf(
            Screens.Tournaments,
            Screens.Players,
            Screens.Rules
        ),
        onClick = navigateTo
    )
}