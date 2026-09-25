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
import com.hardik.safehaven.domain.usecase.ValidateItemUseCase
import com.hardik.safehaven.domain.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AddItemViewModel(
    // private val repository: SecureItemRepositoryForStaticData
    private val addItemUseCase: AddItemUseCase,
    private val validateItemUseCase: ValidateItemUseCase
) : ViewModel() {

    var title by mutableStateOf("")
    var content by mutableStateOf("")
    var type by mutableStateOf(ItemType.NOTE)

    private var imageData: ByteArray? = null
    private var mimeType: String? = null

    fun imageSelected(
        imageBytes: ByteArray?,
        mimeType: String?
    ) {
        imageData = imageBytes
        this.mimeType = mimeType
    }

    private val _error = MutableStateFlow<String?>(null)
    val error : StateFlow<String?> = _error

    fun saveItem(onSuccess: () -> Unit) {
        when (val result = validateItemUseCase(title, content)) {
            is ValidationResult.Error -> {
                _error.value = result.message
            }

            ValidationResult.Success -> {
                viewModelScope.launch {

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