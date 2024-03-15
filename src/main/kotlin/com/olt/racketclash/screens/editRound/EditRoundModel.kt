package com.olt.racketclash.screens.editRound

import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens

class EditRoundModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<EditRoundModel.Model>(navigateToScreen, Model()) {

    data class Model(
        val replaceMe: String = ""
    )
}