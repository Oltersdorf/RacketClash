package com.olt.racketclash.navigation

import com.olt.racketclash.data.Database

sealed class Screens {
    data object Projects : Screens()
    data object NewProject : Screens()
    data class OpenProject(val projectLocation: String, val projectName: String) : Screens()
    data class Teams(val database: Database) : Screens()
}