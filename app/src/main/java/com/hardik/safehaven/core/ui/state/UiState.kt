package com.hardik.safehaven.core.ui.state

sealed class UiState<out T> { // all possible sub-classes are known at compile time.
    object Loading: UiState<Nothing>() // data is being fetched
    data class Success<T>(val data: T): UiState<T>() // data loaded successfully.
    data class Error(val message: String): UiState<Nothing>() // something failed
    object Empty: UiState<Nothing>() // operation succeeded, but no data exists
}
// this is powerful bcz
//  Only ONE state can exist at a time
//  Impossible states are impossible to represent
//  Compiler forces EXHAUSTIVE WHEN
//  Cleaner UI logic
