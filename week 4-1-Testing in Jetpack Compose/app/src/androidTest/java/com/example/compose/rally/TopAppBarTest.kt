package com.example.compose.rally

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

	@get:Rule
	val composeTestRule = createComposeRule()

	@Test
	fun rallyTopAppBarTest() {

		val allScreens = RallyScreen.values().toList()
		composeTestRule.setContent {
			RallyTheme {
				RallyTopAppBar(
					allScreens = allScreens,
					onTabSelected = {  },
					currentScreen = RallyScreen.Accounts
				)
			}
		}

		composeTestRule
			.onNodeWithContentDescription(RallyScreen.Accounts.name)
			.assertIsSelected()
	}

	@Test
	fun rallyTopAppBar_currentLabelExists() {
		val allScreens = RallyScreen.values().toList()
		composeTestRule.setContent {
			RallyTheme {
				RallyTopAppBar(
					allScreens = allScreens,
					onTabSelected = {  },
					currentScreen = RallyScreen.Accounts
				)
			}
		}

		composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

		composeTestRule
			.onNode(
				matcher = hasText(RallyScreen.Accounts.name.uppercase()) and
						hasParent(
							hasContentDescription(RallyScreen.Accounts.name)
						),
				useUnmergedTree = true
			)
			.assertExists()
	}

	@Test
	fun rallyTopAppBar_selectTest() {
		val allScreens = RallyScreen.values().toList()
		composeTestRule.setContent {
			RallyTheme {
				var currentScreen by rememberSaveable { mutableStateOf(RallyScreen.Overview) }

				RallyTopAppBar(
					allScreens = allScreens,
					onTabSelected = { screen -> currentScreen = screen },
					currentScreen = currentScreen
				)
			}
		}

		composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

		composeTestRule
			.onNodeWithContentDescription(RallyScreen.Overview.name)
			.performClick()
			.assertIsSelected()

		composeTestRule
			.onNodeWithContentDescription(RallyScreen.Accounts.name)
			.performClick()
			.assertIsSelected()

		composeTestRule.onNodeWithContentDescription(RallyScreen.Bills.name)
			.performClick()
			.assertIsSelected()
	}
}