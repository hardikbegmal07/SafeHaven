package com.hardik.safehaven.presentation

import androidx.biometric.BiometricManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hardik.safehaven.core.auth.BiometricHelper

@Composable
fun LockScreen(
    onAuthenticated: () -> Unit,
    onAuthenticationFailed: () -> Unit,
    biometricHelper: BiometricHelper,
    authTrigger: Int
) {
    LaunchedEffect(authTrigger) {
        when (biometricHelper.canAuthenticate()) {

            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricHelper.showBiometricPrompt(
                    onSuccess = onAuthenticated,
                    onError = {
                        onAuthenticationFailed()
                    }
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Navigate to PIN fallback
                onAuthenticationFailed()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // Device unsupported
                onAuthenticationFailed()
            }

            else -> {
                // Generic fallback
                onAuthenticationFailed()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Authenticating",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}