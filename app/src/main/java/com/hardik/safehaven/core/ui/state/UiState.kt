package com.hardik.safehaven.core.ui.state

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(val message: String): UiState<Nothing>()
    object Empty: UiState<Nothing>()
}
// this is powerful bcz
//  Only ONE state can exist at a time
//  Impossible states are impossible to represent
//  Compiler forces exhaustive when
//  Cleaner UI logic
