package com.olt.racketclash.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
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
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Database
import com.olt.racketclash.data.Project
import com.olt.racketclash.model.PlayerModel
import com.olt.racketclash.model.ProjectModal
import com.olt.racketclash.model.RoundsModel
import com.olt.racketclash.model.TeamModel
import org.kodein.di.bindProvider
import java.nio.file.Path

internal typealias addProject = (name: String, location: Path) -> Unit
internal typealias deleteProject = (name: String) -> Unit
internal typealias updateProject = (projectName: String, teamNumber: Int, playerNumber: Int) -> Unit

class ProjectsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ProjectModal>(factory = { ProjectModal() })
        val modal by screenModel.state.collectAsState()

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
                    projects = modal.projects,
                    addProject = screenModel::addProject,
                    deleteProject = screenModel::deleteProject,
                    updateProject = screenModel::updateProject
                )
            }
        }
    }
}

@Composable
private fun ProjectSelect(
    projects: List<Project>,
    addProject: addProject,
    deleteProject: deleteProject,
    updateProject: updateProject
) {
    Scaffold(
        modifier = Modifier
            .padding(bottom = 50.dp)
            .clip(RoundedCornerShape(10.dp))
            .requiredWidthIn(min = 500.dp)
            .fillMaxWidth(0.5f),
        topBar = { ProjectSelectHeader(addProject = addProject, projectNames = projects.map { it.name }) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            tonalElevation = 1.dp
        ) {
            LazyColumnWithScroll(
                modifier = Modifier.padding(5.dp),
                itemsSpacedBy = 10.dp

            ) {
                items(items = projects) {
                    ProjectItem(
                        project = it,
                        deleteProject = deleteProject,
                        updateProject = updateProject
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectSelectHeader(
    projectNames: List<String>,
    addProject: addProject
) {
    val navigator = LocalNavigator.currentOrThrow

    TopAppBar(
        title = { Text("Select Project") },
        actions = {
            IconButton(onClick = { navigator.push(item = NewProjectScreen(addProject = addProject, projectNames = projectNames)) }) {
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
    updateProject: updateProject
) {
    val navigator = LocalNavigator.currentOrThrow

    Card(
        onClick = {
            val database = Database(tournamentPath = project.location)
            navigator.push(
                DINavigationScreen(
                    startScreen = TeamsScreen(),
                    transition = DINavigationScreen.Transition.SlideTransition
                ) {
                    bindProvider { TeamModel(database = database, updateProjectTeams = { updateProject(project.name, it, project.playerNumber) }) }
                    bindProvider { PlayerModel(database = database, updateProjectPlayer = { updateProject(project.name, project.teamNumber, it) }) }
                    bindProvider { RoundsModel(database = database) }
                }
            )
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