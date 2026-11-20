package com.hardik.safehaven

import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.usecase.AddItemUseCase
import com.hardik.safehaven.domain.usecase.GetItemByIdUseCase
import com.hardik.safehaven.domain.usecase.UpdateItemUseCase
import com.hardik.safehaven.domain.usecase.ValidateItemUseCase
import com.hardik.safehaven.feature_add.AddItemViewModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// we mock the use cases because we want to verify whether the viewModel calls them correctly.
// Why do we use coEvery and coVerify?
//   MockK function	            Purpose
//   every { }	        Mock a normal, non-suspending function
//   coEvery { }     	Mock a suspend function
//   verify { }	        Verify a normal function call
//   coVerify { }	    Verify a suspend function call

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AddItemViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mock all dependencies because we are testing AddItemViewModel independently.
    private val addItemUseCase: AddItemUseCase = mockk()
    private val updateItemUseCase: UpdateItemUseCase = mockk()
    private val validateItemUseCase: ValidateItemUseCase = mockk()
    private val getItemByIdUseCase: GetItemByIdUseCase = mockk()

    private lateinit var viewModel: AddItemViewModel

    // Helper function to create test documents.
    private fun createTestSecureItem(
        id: String = "doc_101",
        title: String = "Passport",
        content: String = "Test content",
        type: ItemType = ItemType.ID,
        imageData: ByteArray? = null,
        mimeType: String? = null
    ): SecureItem {

        return SecureItem(
            id = id,
            title = title,
            content = content,
            type = type.name,
            createdAt = 1730000000000L,
            imageData = imageData,
            mimeType = mimeType
        )
    }


}