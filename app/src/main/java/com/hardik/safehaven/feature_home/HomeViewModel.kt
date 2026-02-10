package com.hardik.safehaven.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hardik.safehaven.core.ui.state.UiState
import com.hardik.safehaven.domain.model.SecureItem
import com.hardik.safehaven.domain.usecase.GetItemsUseCase
// import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    // repository: SecureItemRepositoryForStaticData
    getItemsUseCase: GetItemsUseCase
) : ViewModel() { // state survive configuration changes
    // logic is not tied to UI lifecycle
    // Android manages it for us ...

    val uiState: StateFlow<HomeUiState> = getItemsUseCase()
        .map { items -> HomeUiState(
            state = when {
                items.isEmpty() -> UiState.Empty
                else -> UiState.Success(items)
            }
        ) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(state = UiState.Loading)
        )

    // private val _uiState = MutableStateFlow(HomeUiState())
    // val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()
    // if this stateflow emits a new value, UI recompose ...

    // private val _items = MutableStateFlow<List<SecureItem>>(emptyList())
//    val items : StateFlow<List<SecureItem>> =
//        repository.getItems()
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = emptyList()
//            )

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