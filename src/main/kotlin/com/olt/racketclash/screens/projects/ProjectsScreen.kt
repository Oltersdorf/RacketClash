package com.olt.racketclash.screens.projects

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Project
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.LazyTableColumn
import com.olt.racketclash.ui.LazyTableWithScroll

internal typealias deleteProject = (name: String) -> Unit

class ProjectsScreen(private val modelBuilder: () -> ProjectsModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Racket Clash",
                    fontSize = 50.sp,
                    modifier = Modifier.padding(top = 50.dp, bottom = 50.dp)
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
        modifier = Modifier
            .padding(bottom = 50.dp)
            .clip(RoundedCornerShape(10.dp))
            .requiredWidthIn(min = 500.dp)
            .fillMaxWidth(0.5f),
        topBar = { ProjectSelectHeader(navigateTo = navigateTo) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
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
                ),
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
        actions = {
            IconButton(onClick = { navigateTo(Screens.NewProject, navigator) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
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
            IconButton(
                onClick = { deleteProject(project.name) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}