package com.olt.racketclash.app.screens.projects

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.data.Project
import com.olt.racketclash.language.Language
import com.olt.racketclash.app.Screens
import com.olt.racketclash.ui.*

@Composable
fun ProjectsScreen(
    language: Language,
    availableLanguages: List<String>,
    projects: List<Project>,
    navigateTo: (Screens) -> Unit,
    changeLanguage: (String) -> Unit,
    deleteProject: (Project) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                Spacer(modifier = Modifier.weight(1.0f))
                DropDownMenu(
                    label = language.language,
                    items = availableLanguages,
                    value = language.languageName,
                    textMapper = { it },
                    onClick = { changeLanguage(it) }
                )
            }

            SettingsView {
                ProjectSelect(
                    language = language,
                    projects = projects,
                    navigateTo = navigateTo,
                    deleteProject = deleteProject
                )
            }
        }
    }
}

@Composable
private fun ProjectSelect(
    language: Language,
    projects: List<Project>,
    navigateTo: (Screens) -> Unit,
    deleteProject: (Project) -> Unit
) {
    Text(
        text = "Racket Clash",
        style = MaterialTheme.typography.headlineLarge
    )

    LazyTableWithScrollScaffold(
        topBarTitle = language.selectProject,
        topBarActions = { AddButton { navigateTo(Screens.NewProject()) } },
        items = projects,
        itemsSpacedBy = 10.dp,
        showHeader = false,
        drawDividers = false,
        columns = listOf(
            LazyTableColumn.Builder { item, _ ->
                ProjectItem(
                    language = language,
                    project = item,
                    navigateTo = navigateTo,
                    deleteProject = deleteProject
                )
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectItem(
    language: Language,
    project: Project,
    navigateTo: (Screens) -> Unit,
    deleteProject: (Project) -> Unit
) {
    Card(
        onClick = {
            navigateTo(Screens.OpenProject(project = project))
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(5.dp)
        ) {
            Column {
                Text(project.name)
                Text(project.lastModified)
            }
            Spacer(modifier = Modifier.weight(weight = 1.0f))
            Column {
                Text("${language.players}: ${project.playerNumber}")
                Text("${language.teams}: ${project.teamNumber}")
            }
            Spacer(modifier = Modifier.weight(weight = 1.0f))
            DeleteButton { deleteProject(project) }
        }
    }
}