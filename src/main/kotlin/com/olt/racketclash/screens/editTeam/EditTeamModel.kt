package com.olt.racketclash.screens.editTeam

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.olt.racketclash.database.Database
import com.olt.racketclash.data.Team
import com.olt.racketclash.navigation.NavigableStateScreenModel
import com.olt.racketclash.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditTeamModel(
    navigateToScreen: (Screens, Navigator) -> Unit,
    private val database: Database,
    team: Team?
) : NavigableStateScreenModel<EditTeamModel.Model>(navigateToScreen, Model(team = team)) {

    data class Model(
        val team: Team?
    )

    fun updateTeam(id: Long?, name: String, strength: Int) {
        screenModelScope.launch(context = Dispatchers.IO) {
            if (id == null)
                database.addTeam(name = name, strength = strength)
            else
                database.updateTeam(id = id, name = name, strength = strength)
        }
    }
}