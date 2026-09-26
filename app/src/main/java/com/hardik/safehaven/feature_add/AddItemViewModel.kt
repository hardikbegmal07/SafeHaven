package com.hardik.safehaven.feature_add

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.safehaven.data.security.toByteArray
import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.usecase.AddItemUseCase
import com.hardik.safehaven.domain.usecase.GetItemByIdUseCase
import com.hardik.safehaven.domain.usecase.UpdateItemUseCase
import com.hardik.safehaven.domain.usecase.ValidateItemUseCase
import com.hardik.safehaven.domain.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class AddItemViewModel(
    // private val repository: SecureItemRepositoryForStaticData
    private val addItemUseCase: AddItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val validateItemUseCase: ValidateItemUseCase,
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val editItemId: String? = null
) : ViewModel() {

    var title by mutableStateOf("")
    var content by mutableStateOf("")
    var type by mutableStateOf(ItemType.NOTE)

    private var imageData: ByteArray? = null
    private var mimeType: String? = null

    var existingImageData by mutableStateOf<ByteArray?>(null)
        private set
    // used only for displaying the existing image when editing

    val isEditMode: Boolean
        get() = editItemId != null
    // true when AddItemScreen was opened through Edit.

    private val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> = _error

    init {

        /**
           If editItemId is null: Normal Add flow.

           If editItemId exists: Load existing item and fill the fields.
        */

        editItemId?.let { id ->
            viewModelScope.launch {
                getItemByIdUseCase(id)
                    .first()
                    ?.let { item ->
                        title = item.title
                        content = item.content

                        type = try {
                            ItemType.valueOf(item.type)
                        } catch (e: Exception) {
                            ItemType.NOTE
                        }

                        imageData = item.imageData
                        mimeType = item.mimeType
                        existingImageData = item.imageData
                    }
            }
        }

    }

    fun imageSelected(
        imageBytes: ByteArray?,
        mimeType: String?
    ) {
        imageData = imageBytes
        this.mimeType = mimeType

        /** Remove old image preview once a new image has been selected */
        existingImageData = null
    }

    fun saveItem(onSuccess: () -> Unit) {
        when (val result = validateItemUseCase(title, content)) {
            is ValidationResult.Error -> {
                _error.value = result.message
            }

            ValidationResult.Success -> {
                viewModelScope.launch {

                    if (editItemId == null) {

                        val item = SecureItem(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            content = content,
                            type = type.name,
                            createdAt = System.currentTimeMillis(),
                            imageData = imageData,
                            mimeType = mimeType
                        )

                        addItemUseCase(item)

                    } else {

                        val existingItem = getItemByIdUseCase(editItemId).first()

                        if (existingItem != null) {

                            val updatedItem = existingItem.copy(
                                title = title,
                                content = content,
                                type = type.name,
                                imageData = imageData,
                                mimeType = mimeType
                            )

                            updateItemUseCase(updatedItem)
                        }

                    }

                    onSuccess() // navigate after saving
                }
            }
        }


//        val item = SecureItem(
//            id = UUID.randomUUID().toString(),
//            title = title,
//            content = content,
//            type = type,
//            createdAt = System.currentTimeMillis()
//        )

        // repository.addItem(item)
    }
}