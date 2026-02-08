package com.hardik.safehaven.feature_view

import androidx.lifecycle.ViewModel
import com.hardik.safehaven.data.repository.SecureItemRepositoryForStaticData
import com.hardik.safehaven.domain.model.SecureItem

class ViewItemViewModel(
    private val repository: SecureItemRepositoryForStaticData,
    private val itemId: String
) : ViewModel() {

    val item: SecureItem? = repository.getItemById(itemId)

    fun deleteItem() {
        repository.deleteItem(itemId)
    }
}