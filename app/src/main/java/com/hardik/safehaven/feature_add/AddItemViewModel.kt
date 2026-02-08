package com.hardik.safehaven.feature_add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hardik.safehaven.data.repository.SecureItemRepositoryForStaticData
import com.hardik.safehaven.domain.model.SecureItem
import java.util.UUID

class AddItemViewModel(
    private val repository: SecureItemRepositoryForStaticData
) : ViewModel() {

    var title by mutableStateOf("")
    var content by mutableStateOf("")
    var type by mutableStateOf("")

    fun saveItem() {
        val item = SecureItem(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            type = type,
            createdAt = System.currentTimeMillis()
        )

        repository.addItem(item)
    }
}