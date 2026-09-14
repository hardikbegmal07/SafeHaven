package com.hardik.safehaven.feature_auth.auth

sealed class AuthState {
    data object Locked: AuthState() // App is waiting for biometric
    data object Unlocked: AuthState() // User successfully authenticated

    data object LoginRequired : AuthState() // Biometric wasn't used / succeeded, so show normal login
}