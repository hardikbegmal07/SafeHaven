package com.hardik.safehaven

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.feature_add.AddItemViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddItemScreenTest {

    @get:Rule
    val composeTestTule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: AddItemViewModel

    // snapshot-backed state for viewModel properties
    private val titleState = androidx.compose.runtime.mutableStateOf("")
    private val contentState = androidx.compose.runtime.mutableStateOf("")
    private val typeState = androidx.compose.runtime.mutableStateOf(ItemType.NOTE)
    private val existingImageState = androidx.compose.runtime.mutableStateOf<ByteArray?>(null)

    private val errorFlow = MutableStateFlow<String?>(null)



}