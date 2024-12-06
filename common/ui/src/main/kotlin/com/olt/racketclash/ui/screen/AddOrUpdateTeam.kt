package com.olt.racketclash.ui.screen

import androidx.compose.runtime.*
import com.olt.racketclash.addorupdateteam.AddOrUpdateTeamModel
import com.olt.racketclash.database.Database
import com.olt.racketclash.ui.layout.Form
import com.olt.racketclash.ui.layout.FormNumberSelector
import com.olt.racketclash.ui.layout.FormRow
import com.olt.racketclash.ui.layout.FormTextField

@Composable
internal fun AddOrUpdateTeam(
    database: Database,
    teamId: Long?,
    teamName: String?,
    tournamentId: Long,
    navigateBack: () -> Unit
) {
    val model = remember { AddOrUpdateTeamModel(
        database = database,
        teamId = teamId,
        tournamentId = tournamentId
    ) }
    val state by model.state.collectAsState()

    Form(
        title = teamName ?: "New team",
        isLoading = state.isLoading,
        isSavable = state.isSavable,
        onSave = {
            model.save()
            navigateBack()
        }
    ) {
        FormRow {
            FormTextField(
                value = state.team.name,
                label = "Name",
                isError = !state.isSavable,
                onValueChange = model::updateName
            )

            FormNumberSelector(
                value = state.team.rank,
                label = "Rank",
                range = 1..Int.MAX_VALUE,
                onUp = model::updateRank,
                onDown = model::updateRank
            )
        }
    }
}