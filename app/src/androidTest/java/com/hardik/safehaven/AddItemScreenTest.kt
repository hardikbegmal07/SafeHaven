package com.hardik.safehaven

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.feature_add.AddItemScreenContent
import com.hardik.safehaven.feature_add.AddItemViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddItemScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // displays AddItemScreenContent with the supplied state
    private fun launchAddItemScreenContent(
        content: @Composable () -> Unit
    ) {
        composeTestRule.setContent {
            content()
        }
    }

    @Test
    fun addItemScreen_displaysAllMainComponent() {

        launchAddItemScreenContent {
            AddItemScreenContent(
                title = "",
                content = "",
                type = ItemType.NOTE,
                error = null,
                existingImageData = null,
                selectedImageUri = null,
                onTitleChange = {},
                onContentChange = {},
                onTypeChange = {},
                onUploadClick = {},
                onSaveClick = {}
            )
        }

        composeTestRule.apply {
            onNodeWithText("Add a Doc").assertIsDisplayed()
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Content").assertIsDisplayed()
            onNodeWithText("Item Type").assertIsDisplayed()
            onNodeWithText("Upload a Doc").assertIsDisplayed()
            onNodeWithText("Save").assertIsDisplayed()
        }

    }

    // Title field test
    @Test
    fun addItemScreen_titleFieldAcceptsUserInput() {

        val titleState = mutableStateOf("")

        launchAddItemScreenContent {
            AddItemScreenContent(
                title = titleState.value,
                content = "",
                type = ItemType.NOTE,
                error = null,
                existingImageData = null,
                selectedImageUri = null,
                onTitleChange = {
                    titleState.value = it
                },
                onContentChange = {},
                onTypeChange = {},
                onUploadClick = {},
                onSaveClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("title_field")
            .performTextInput("Passport")

        composeTestRule.waitForIdle()

        assertEquals("Passport", titleState.value)
    }

    // Content field tests
    @Test
    fun addItemScreen_contentFieldAcceptsUserInput() {

        val contentState = mutableStateOf("")

        launchAddItemScreenContent {
            AddItemScreenContent(
                title = "",
                content = contentState.value,
                type = ItemType.NOTE,
                error = null,
                existingImageData = null,
                selectedImageUri = null,
                onTitleChange = {},
                onContentChange = {
                    contentState.value = it
                },
                onTypeChange = {},
                onUploadClick = {},
                onSaveClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("content_field")
            .performTextInput("Passport details")

        composeTestRule.waitForIdle()

        assertEquals("Passport details", contentState.value)
    }

    // Item type dropdown tests
    @Test
    fun addItemScreen_itemTypeDropdownOpens() {

        launchAddItemScreenContent{
            AddItemScreenContent(
                title = "",
                content = "",
                type = ItemType.NOTE,
                error = null,
                existingImageData = null,
                selectedImageUri = null,
                onTitleChange = {},
                onContentChange = {},
                onTypeChange = {},
                onUploadClick = {},
                onSaveClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("item_type_dropdown")
            .performClick()

        composeTestRule.waitForIdle()

        // Check that the dropdown options are displayed.
        // The currently selected NOTE is displayed both
        // in the field and in the dropdown menu.

        ItemType.entries.forEach { itemType ->

            val expectedCount =
                if (itemType == ItemType.NOTE) 2 else 1

            composeTestRule
                .onAllNodesWithText(itemType.name)
                .assertCountEquals(expectedCount)
        }
    }

    // Item type selection test
    @Test
    fun addItemScreen_userCanSelectDifferentItemType() {

        val selectedTypeState =
            mutableStateOf(ItemType.NOTE)

//        val newType = ItemType.entries.firstOrNull {
//            it != ItemType.NOTE
//        } ?: error("A second ItemType is required for this test")

        launchAddItemScreenContent{

            AddItemScreenContent(
                title = "",
                content = "",
                type = selectedTypeState.value,
                error = null,
                existingImageData = null,
                selectedImageUri = null,
                onTitleChange = {},
                onContentChange = {},
                onTypeChange = {
                    selectedTypeState.value = it
                },
                onUploadClick = {},
                onSaveClick = {}
            )

        }

        // Open dropdown

        composeTestRule
            .onNodeWithTag("item_type_dropdown")
            .assertTextContains("NOTE")

        // Open dropdown.
        composeTestRule
            .onNodeWithTag("item_type_dropdown")
            .performClick()


        // Select another item type
        composeTestRule
            .onNodeWithText("PASSWORD")
            .performClick()

        composeTestRule.waitForIdle()

        assertEquals(
            ItemType.PASSWORD,
            selectedTypeState.value
        )

        // Verify selected value is displayed in the field

        composeTestRule
            .onNodeWithTag("item_type_dropdown")
            .assertTextContains("PASSWORD")
    }

    // need to update the test cases for remaining screen and then viewModel as well

}