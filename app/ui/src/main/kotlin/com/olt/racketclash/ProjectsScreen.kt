package com.olt.racketclash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.olt.racketclash.data.Project
import com.olt.racketclash.language.Language
import com.olt.racketclash.navigate.Screens
import com.olt.racketclash.projects.ProjectsModel
import com.olt.racketclash.ui.*

@Composable
fun ProjectsScreen(
    model: ProjectsModel,
    language: Language,
    availableLanguages: List<String>,
    navigateTo: (Screens) -> Unit,
    changeLanguage: (String) -> Unit
) {
    val state by model.state.collectAsState()

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
                    projects = state.projects,
                    navigateTo = navigateTo,
                    deleteProject = model::deleteProject
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
    deleteProject: (Long) -> Unit
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

@Composable
private fun ProjectItem(
    language: Language,
    project: Project,
    navigateTo: (Screens) -> Unit,
    deleteProject: (Long) -> Unit
) {
    Card(
        onClick = {
            navigateTo(Screens.Teams(projectId = project.id))
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
                Text("${language.players}: ${project.numberOfPlayers}")
                Text("${language.teams}: ${project.numberOfTeams}")
            }
            Spacer(modifier = Modifier.weight(weight = 1.0f))
            DeleteButton { deleteProject(project.id) }
        }
    }
}