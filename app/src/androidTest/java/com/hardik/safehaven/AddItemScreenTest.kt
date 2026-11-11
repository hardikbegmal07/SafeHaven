package com.hardik.safehaven

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.feature_add.AddItemScreenContent
import com.hardik.safehaven.feature_add.AddItemViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddItemScreenTest {

    @get:Rule
    val composeTestTule = createAndroidComposeRule<ComponentActivity>()

    // helper function
    private fun launchAddItemScreenContent(
        title: String = "",
        content : String = "",
        type: ItemType = ItemType.NOTE,
        error: String? = null,
        existingImageData: ByteArray? = null,
        selectedImageUri: Uri? = null,
        onTitleChange: (String) -> Unit = {},
        onContentChange: (String) -> Unit = {},
        onTypeChange: (ItemType) -> Unit = {},
        onUploadClick: () -> Unit = {},
        onSaveClick: () -> Unit = {}
    ) {

        composeTestTule.setContent {

            AddItemScreenContent(

                title = title,
                content = content,
                type = type,
                error = error,

                existingImageData = existingImageData,
                selectedImageUri = selectedImageUri,

                onTitleChange = onTitleChange,
                onContentChange = onContentChange,
                onTypeChange = onTypeChange,

                onUploadClick = onUploadClick,
                onSaveClick = onSaveClick
            )
        }

    }

    @Test
    fun addItemScreen_displaysAllMainComponent() {

        launchAddItemScreenContent()

        composeTestTule.apply {

            onNodeWithText("Add a Doc").assertIsDisplayed()

            onNodeWithText("Title").assertIsDisplayed()

            onNodeWithText("Content").assertIsDisplayed()

            onNodeWithText("Item Type").assertIsDisplayed()

            onNodeWithText("Upload a Doc").assertIsDisplayed()

            onNodeWithText("Save").assertIsDisplayed()

        }

    }

}