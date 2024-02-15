package com.olt.racketclash.ui.navigator

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.olt.racketclash.data.Database

class RacketClashNavigator {

    lateinit var database: Database

    private fun projectScreenNavigate(targets: PScreen.NavigationTargets, navigator: Navigator) {
        when(targets) {
            PScreen.NavigationTargets.NewProjectScreen -> {

            }
            PScreen.NavigationTargets.TeamsScreen -> {
                database = Database("C:\\Users\\f-olt\\Desktop\\Test")
                navigator.push(TestScreen(database))
            }
        }
    }

    @Composable
    fun Navigator() {
        ScreenNavigator(initialScreen = PScreen(navigateTo = ::projectScreenNavigate))
    }
}

class TestScreen(val database: Database) : Screen {

    @Composable
    override fun Content() {
        database.teams()
    }
}

class PScreen(private val navigateTo: (NavigationTargets, Navigator) -> Unit) : Screen {

    sealed class NavigationTargets {
        data object NewProjectScreen : NavigationTargets()
        data object TeamsScreen : NavigationTargets()
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Button(onClick = { navigateTo(NavigationTargets.TeamsScreen, navigator) }) {
            Text("Test")
        }
    }
}