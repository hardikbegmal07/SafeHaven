package com.hardik.safehaven

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hardik.safehaven.core.ui.state.UiState
import com.hardik.safehaven.feature_home.HomeScreenContent
import com.hardik.safehaven.feature_home.HomeUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTextRule = createComposeRule()

    @Test
    fun homeScreen_emptyState_displayMessage() {

        composeTextRule.setContent {

            HomeScreenContent(
                uiState = HomeUiState(UiState.Empty),
                onAddClick = {},
                onItemClick = {},
            )

        }

        composeTextRule
            .onNodeWithText("No secure Items yet")
            .assertIsDisplayed()

    }

}