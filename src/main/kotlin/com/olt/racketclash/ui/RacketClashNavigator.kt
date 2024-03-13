package com.olt.racketclash.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.olt.racketclash.navigation.RootNavigator

@Composable
fun RacketClashNavigator(rootNavigator: RootNavigator) {
    Navigator(screen = rootNavigator.defaultScreen()) { navigator ->
        SlideTransition(navigator)
    }
}