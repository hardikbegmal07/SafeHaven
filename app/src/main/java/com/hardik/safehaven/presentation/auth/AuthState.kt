package com.hardik.safehaven.presentation.auth

sealed class AuthState {
    object Locked: AuthState()
    object Unlocked: AuthState()
}