package com.hardik.safehaven

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hardik.safehaven.core.ui.state.UiState
import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.feature_home.HomeScreenContent
import com.hardik.safehaven.feature_home.HomeUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createTestSecureItem(
        id: String = "doc_101",
        title: String = "Passport",
        content: String = "Test passport content",
        type: ItemType = ItemType.NOTE,
        createdAt: Long = 1730000000000L,
        imageData: ByteArray? = null,
        mimeType: String? = null
    ) : SecureItem {

        return SecureItem(
            id = id,
            title = title,
            content = content,
            type = type.name,
            createdAt = createdAt,
            imageData = imageData,
            mimeType = mimeType
        )
    }

    @Test
    fun homeScreen_emptyState_displayMessage() {

        composeTestRule.setContent {

            HomeScreenContent(
                uiState = HomeUiState(UiState.Empty),
                onAddClick = {},
                onItemClick = {},
            )

        }

        composeTestRule
            .onNodeWithText("No secure Items yet")
            .assertIsDisplayed()

    }

    @Test
    fun homeScreen_loadingState_displayProgressIndicator() {

        composeTestRule.setContent {

            HomeScreenContent(
                uiState = HomeUiState(state = UiState.Loading),
                onAddClick = {},
                onItemClick = {}
            )
        }

        composeTestRule
            .onNode(hasTestTag("loading_indicator"))
            .assertIsDisplayed()

    }

    @Test
    fun homeScreen_errorState_displaysErrorMessage() {

        val errorMessage = "Unable to load secure documents"

        composeTestRule.setContent {

            HomeScreenContent(
                uiState = HomeUiState(
                    state = UiState.Error(errorMessage)
                ),
                onAddClick = {},
                onItemClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()

    }

    @Test
    fun homeScreen_addButton_invokesCallback() {

        var addClicked = false

        composeTestRule.setContent {

            HomeScreenContent(
                uiState = HomeUiState(state = UiState.Empty),
                onAddClick = { addClicked = true },
                onItemClick = {}
            )

        }

        composeTestRule
            .onNodeWithContentDescription("Add")
            .performClick()

        assertTrue(addClicked)

    }

    @Test
    fun homeScreen_documentClick_passesCorrectId() {

        var selectedItemId: String? = null

        // creating a test SecureItem using the actual model constructor
        val note = createTestSecureItem(
            id = "doc_101",
            title = "Personal Notes",
            type = ItemType.NOTE
        )

        val password = createTestSecureItem(
            id = "doc_102",
            title = "Gmail Password",
            type = ItemType.PASSWORD
        )

        val identity = createTestSecureItem(
            id = "doc_103",
            title = "Passport",
            type = ItemType.ID
        )

        composeTestRule.setContent {

            HomeScreenContent(
                uiState = HomeUiState(
                    state = UiState.Success(listOf(note, password, identity))
                ),
                onAddClick = {},
                onItemClick = { id ->
                    selectedItemId = id
                },
            )

        }

        composeTestRule
            .onNodeWithTag("item_doc_101")
            .performClick()

        // Assert
        assertEquals("doc_101", selectedItemId)
        // callback - lets us verify that the correct document ID is passed

    } // this test verifies that the correct document is selected, not just something was clicked.

}