package com.hardik.safehaven.presentation

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
    biometricHelper: BiometricHelper
) {
    LaunchedEffect(Unit) {
        biometricHelper.showBiometricPrompt(
            onSuccess = { onAuthenticated() },
            onError = {  }
        )
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