package com.hardik.safehaven.presentation

import androidx.lifecycle.ViewModel
import com.hardik.safehaven.presentation.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Locked)

    val authState: StateFlow<AuthState> = _authState

    fun unlock() {
        _authState.value = AuthState.Unlocked
    }

    fun lock() {
        _authState.value = AuthState.Locked
    }
}