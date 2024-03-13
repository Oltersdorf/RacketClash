package com.olt.racketclash.navigation

import com.olt.racketclash.data.Database
import java.nio.file.Path

sealed class Screens {
    data object Projects : Screens()
    data class NewProject(val projectNames: List<String>, val addProject: (name: String, location: Path) -> Unit) : Screens()
    data class OpenProject(val projectLocation: String) : Screens()
    data class Teams(val database: Database) : Screens()
}