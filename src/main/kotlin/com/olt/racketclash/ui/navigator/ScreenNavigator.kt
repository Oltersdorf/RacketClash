package com.olt.racketclash.ui.navigator

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.SlideTransition

sealed class Transition {
    data object FadeTransition : Transition()
    data object SlideTransition : Transition()
    data object ScaleTransition : Transition()
    data class CustomTransition(val transition: AnimatedContentTransitionScope<Screen>.() -> ContentTransform) : Transition()
}

@Composable
fun ScreenNavigator(initialScreen: Screen, transition: Transition? = null) {
    Navigator(screen = initialScreen) { navigator: Navigator ->
        when (transition) {
            is Transition.CustomTransition -> ScreenTransition(navigator = navigator, transition = transition.transition)
            Transition.FadeTransition -> FadeTransition(navigator = navigator)
            Transition.ScaleTransition -> ScaleTransition(navigator = navigator)
            Transition.SlideTransition -> SlideTransition(navigator = navigator)
            null -> {}
        }
    }
}