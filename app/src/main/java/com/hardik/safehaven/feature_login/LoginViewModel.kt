package com.hardik.safehaven.feature_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun onUsernameChanged(value: String) {
        username = value
    }

    fun onPasswordChanged(value: String) {
        password = value
    }

    fun login(
        onSuccess: () -> Unit
    ) {
        // Perform login/ API request

        val loginSuccessful = true

        if (loginSuccessful) {
            onSuccess()
        }

    }

}