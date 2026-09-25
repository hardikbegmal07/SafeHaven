package com.hardik.safehaven.feature_auth

import androidx.lifecycle.ViewModel
import com.hardik.safehaven.feature_auth.auth.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unlocked)

    val authState: StateFlow<AuthState> = _authState

    private val _authTrigger = MutableStateFlow(0)

    val authTrigger: StateFlow<Int> = _authTrigger.asStateFlow()

    fun unlock() {
        _authState.value = AuthState.Unlocked
    }

    fun requireLogin() {
        _authState.value = AuthState.LoginRequired
    }

    fun lock() {
        _authState.value = AuthState.Locked
        _authTrigger.value++
    }
}