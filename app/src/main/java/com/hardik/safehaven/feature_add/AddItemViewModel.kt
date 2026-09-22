package com.hardik.safehaven.feature_add

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    fun imageSelected(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    private val _cameraImageUri = MutableStateFlow<Uri?>(null)
    val cameraImageUri = _cameraImageUri.asStateFlow()

    fun cameraImageSelected(uri: Uri?) {
        _cameraImageUri.value = uri
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
                    addItemUseCase(
                        SecureItem(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            content = content,
                            type = type.name,
                            createdAt = System.currentTimeMillis()
                        )
                    )
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