package com.olt.racketclash.screens.projects

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                Text(
                    text = "Racket Clash",
                    style = MaterialTheme.typography.headlineLarge
                )
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
    Scaffold(
        modifier = Modifier.clip(RoundedCornerShape(10.dp)).requiredHeightIn(max = 500.dp),
        topBar = { ProjectSelectHeader(navigateTo = navigateTo) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            tonalElevation = 1.dp
        ) {
            LazyTableWithScroll(
                items = projects,
                showHeader = false,
                modifier = Modifier.padding(5.dp),
                itemsSpacedBy = 10.dp,
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectSelectHeader(
    navigateTo: (Screens, Navigator) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow

    TopAppBar(
        title = { Text("Select Project") },
        actions = { AddButton { navigateTo(Screens.NewProject, navigator) } },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
            navigateTo(Screens.OpenProject(projectLocation = project.location, projectName = project.name), navigator)
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