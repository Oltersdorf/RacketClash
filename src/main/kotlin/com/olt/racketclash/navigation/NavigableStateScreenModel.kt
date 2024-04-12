package com.olt.racketclash.navigation

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.update

abstract class NavigableStateScreenModel<S>(
    private val navigateToScreen: (Screens, Navigator) -> Unit,
    initialState: S
) : StateScreenModel<S>(initialState = initialState) {
    fun navigateTo(screen: Screens, navigator: Navigator) = navigateToScreen(screen, navigator)

    fun updateState(block: S.() -> S) {
        mutableState.update(block)
    }
}