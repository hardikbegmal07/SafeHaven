package com.hardik.safehaven.feature_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.safehaven.core.ui.state.UiState
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.usecase.DeleteItemUseCase
import com.hardik.safehaven.domain.usecase.GetItemByIdUseCase
import com.hardik.safehaven.domain.usecase.GetItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewItemViewModel(
    private val itemId: String,
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

//    val item: StateFlow<SecureItem?> = getItemUseCase(itemId)
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = null
//        )

    val uiState: StateFlow<UiState<SecureItem>> =
        getItemByIdUseCase(itemId)
            .map { item ->
                if (item == null) {
                    UiState.Error("Item not found")
                } else {
                    UiState.Success(item)
                }
            }
            .onStart {
                emit(UiState.Loading)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState.Loading
            )

    fun deleteItem() {
//        repository.deleteItem(itemId)
        viewModelScope.launch {
                deleteItemUseCase(itemId)
        }
    }
}