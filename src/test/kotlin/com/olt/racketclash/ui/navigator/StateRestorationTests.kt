package com.olt.racketclash.ui.navigator

import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import org.junit.Test

class StateRestorationTests {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun stateRestoration() {
        runDesktopComposeUiTest {
            val restorationTester = StateRestorationTester(composeTest = this)

            restorationTester.setContent {
                val rc = remember(key1 = "") { RacketClashNavigator() }
                rc.Navigator()
            }

            onNodeWithText("Test").performClick()

            restorationTester.emulateSaveAndRestore()
        }
    }
}