package com.hardik.safehaven.feature_home

import androidx.lifecycle.ViewModel
import com.hardik.safehaven.data.repository.SecureItemRepository
import com.hardik.safehaven.domain.model.SecureItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val repository: SecureItemRepository
) : ViewModel() { // state survive configuration changes
    // logic is not tied to UI lifecycle
    // Android manages it for us ...

    // private val _uiState = MutableStateFlow(HomeUiState())
    // val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()
    // if this stateflow emits a new value, UI recompose ...

    private val _items = MutableStateFlow<List<SecureItem>>(emptyList())
    val items : StateFlow<List<SecureItem>> = _items.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        _items.value = repository.getItems()
    }

//    fun loadItems() {
//        _uiState.update {
//            it.copy(
//                items = listOf("Passport", "Aadhar card", "Bank Card"),
//                isLoading = false
//            )
//        }
//    } // takes old state -> produces new state
}

// ui does not ask for data directly
// it reacts to state