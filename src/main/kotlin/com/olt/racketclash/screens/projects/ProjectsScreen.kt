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
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Project
import com.olt.racketclash.language.translations.Language
import com.olt.racketclash.navigation.Screens
import com.olt.racketclash.ui.*

class ProjectsScreen(private val modelBuilder: () -> ProjectsModel) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { modelBuilder() }
        val stateModel by screenModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Spacer(modifier = Modifier.weight(1.0f))
                    DropDownMenu(
                        label = stateModel.language.language,
                        items = stateModel.availableLanguages,
                        value = stateModel.selectedLanguage,
                        textMapper = { it },
                        onClick = screenModel::changeLanguage
                    )
                }

                SettingsView {
                    ProjectSelect(
                        model = stateModel,
                        screenModel = screenModel
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectSelect(
    model: ProjectsModel.Model,
    screenModel: ProjectsModel
) {
    Text(
        text = "Racket Clash",
        style = MaterialTheme.typography.headlineLarge
    )

    val navigator = LocalNavigator.currentOrThrow
    LazyTableWithScrollScaffold(
        topBarTitle = model.language.selectProject,
        topBarActions = { AddButton { screenModel.navigateTo(Screens.NewProject(language = model.language), navigator) } },
        items = model.projects,
        itemsSpacedBy = 10.dp,
        showHeader = false,
        drawDividers = false,
        columns = listOf(
            LazyTableColumn.Builder { item, _ ->
                ProjectItem(
                    language = model.language,
                    project = item,
                    screenModel = screenModel
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
    screenModel: ProjectsModel
) {
    val navigator = LocalNavigator.currentOrThrow

    Card(
        onClick = {
            screenModel.navigateTo(Screens.OpenProject(project = project, language = language), navigator)
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
            DeleteButton { screenModel.deleteProject(project.name) }
        }
    }
}