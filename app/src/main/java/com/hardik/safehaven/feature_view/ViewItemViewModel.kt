package com.hardik.safehaven.feature_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.usecase.DeleteItemUseCase
import com.hardik.safehaven.domain.usecase.GetItemByIdUseCase
import com.hardik.safehaven.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewItemViewModel(
    private val itemId: String,
    private val getItemUseCase: GetItemByIdUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

    val item: StateFlow<SecureItem?> = getItemUseCase(itemId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun deleteItem() {
        //repository.deleteItem(itemId)
        viewModelScope.launch {
            deleteItemUseCase(itemId)
        }
    }
}