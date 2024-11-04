package com.olt.racketclash.ui.screen

import androidx.compose.runtime.Composable
import com.olt.racketclash.ui.layout.NavigationList
import com.olt.racketclash.ui.navigate.Screens

@Composable
internal fun RacketClash(navigateTo: (Screens) -> Unit) {
    NavigationList(
        navList = listOf(
            Screens.Tournaments,
            Screens.Players,
            Screens.GameRules
        ),
        onClick = navigateTo
    )
}