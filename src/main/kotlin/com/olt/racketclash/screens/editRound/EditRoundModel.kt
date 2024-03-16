package com.olt.racketclash.screens.editRound

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.data.Database
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRoundModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database
) : NavigableStateScreenModel<EditRoundModel.Model>(navigateToScreen, Model()) {

    data class Model(
        val replaceMe: String = ""
    )

    fun addRound(name: String) {
        screenModelScope.launch(context = Dispatchers.IO) {
            database.addRound(name = name)
        }
    }
}