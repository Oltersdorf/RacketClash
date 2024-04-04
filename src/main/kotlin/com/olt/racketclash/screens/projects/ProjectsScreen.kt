package com.olt.racketclash.screens.projects

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Project
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

internal typealias deleteProject = (name: String) -> Unit

class ProjectsScreen(private val modelBuilder: () -> ProjectsModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsView {
                ProjectSelect(
                    projects = stateModel.projects,
                    deleteProject = screenModel::deleteProject,
                    navigateTo = screenModel::navigateTo
                )
            }
        }
    }
}

@Composable
private fun ProjectSelect(
    projects: List<Project>,
    deleteProject: deleteProject,
    navigateTo: (Screens, Navigator) -> Unit
) {
    Text(
        text = "Racket Clash",
        style = MaterialTheme.typography.headlineLarge
    )

    val navigator = LocalNavigator.currentOrThrow
    LazyTableWithScrollScaffold(
        topBarTitle = "Select Project",
        topBarActions = { AddButton { navigateTo(Screens.NewProject, navigator) } },
        items = projects,
        itemsSpacedBy = 10.dp,
        showHeader = false,
        drawDividers = false,
        columns = listOf(
            LazyTableColumn.Builder { item, _ ->
                ProjectItem(
                    project = item,
                    deleteProject = deleteProject,
                    navigateTo = navigateTo
                )
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectItem(
    project: Project,
    deleteProject: deleteProject,
    navigateTo: (Screens, Navigator) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    Card(
        onClick = {
            navigateTo(Screens.OpenProject(project = project), navigator)
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
                Text("Player: ${project.playerNumber}")
                Text("Teams: ${project.teamNumber}")
            }
            Spacer(modifier = Modifier.weight(weight = 1.0f))
            DeleteButton { deleteProject(project.name) }
        }
    }
}