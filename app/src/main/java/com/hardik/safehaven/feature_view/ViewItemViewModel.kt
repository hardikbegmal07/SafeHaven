package com.hardik.safehaven.feature_view

import androidx.lifecycle.ViewModel
import com.hardik.safehaven.data.repository.SecureItemRepository
import com.hardik.safehaven.domain.model.SecureItem

class ViewItemViewModel(
    private val repository: SecureItemRepository,
    private val itemId: String
) : ViewModel() {

    val item: SecureItem? = repository.getItemById(itemId)

    fun deleteItem() {
        repository.deleteItem(itemId)
    }
}