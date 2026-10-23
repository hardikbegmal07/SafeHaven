package com.hardik.safehaven.feature_login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val auth: FirebaseAuth) : ViewModel() {

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onUsernameChanged(value: String) {
        username = value
        _error.value = null
    }

    fun onPasswordChanged(value: String) {
        password = value
        _error.value = null
    }

    fun onConfirmPasswordChanged(value: String) {
        confirmPassword = value
        _error.value = null
    }

    fun login(
        onSuccess: () -> Unit
    ) {
        // Perform login/ API request
        val email = username.trim()

        if (email.isEmpty()) {
            _error.value = "Please enter your email."
            return
        }

        if (password.isEmpty()) {
            _error.value = "Please enter your password."
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _error.value = "Please enter a valid email."
            return
        }

        _isLoading.value = true
        _error.value = null

        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->
            _isLoading.value = false

            if (task.isSuccessful) {
                onSuccess()
            } else {

                _error.value = getFirebaseErrorMessage(task.exception)

            }
        }

    }

    fun signUp(onSuccess: () -> Unit) {

        val email = username.trim()

        if (email.isEmpty()) {
            _error.value = "Please enter your email."
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            _error.value = "Please enter a valid email."
            return
        }

        if (password.isEmpty()) {
            _error.value = "Please enter a password."
            return
        }

        if (password.length < 6) {
            _error.value = "Password must be at least 6 characters."
            return
        }

        if (password != confirmPassword) {
            _error.value = "Passwords do not match."
            return
        }

        _isLoading.value = true
        _error.value = null

        auth.createUserWithEmailAndPassword(
            email,
            password
        )
            .addOnCompleteListener { task ->

                _isLoading.value = false

                if (task.isSuccessful) {

                    /*
                     * Firebase automatically signs the
                     * newly created user in.
                     */
                    onSuccess()

                } else {

                    _error.value =
                        getFirebaseErrorMessage(
                            task.exception
                        )
                }
            }

    }

    private fun getFirebaseErrorMessage(exception: Exception?): String {

        return when (exception?.message?.lowercase()) {
            null -> {
                ""
            }

            else -> {
                when {
                    exception.message!!
                        .contains(
                            "email address is badly formatted",
                            ignoreCase = true
                        ) -> {
                        "Invalid email address."
                    }

                    exception.message!!
                        .contains(
                            "password is invalid",
                            ignoreCase = true
                        ) -> {
                        "Incorrect email or password."
                    }

                    exception.message!!
                        .contains(
                            "no user record",
                            ignoreCase = true
                        ) -> {
                        "No account found with this email."
                    }

                    exception.message!!
                        .contains(
                            "email address is already in use",
                            ignoreCase = true
                        ) -> {
                        "An account already exists with this email."
                    }

                    exception.message!!
                        .contains(
                            "network",
                            ignoreCase = true
                        ) -> {
                        "Network error. Please try again."
                    }

                    else -> {
                        "Authentication failed. Please try again."
                    }
                }
            }
        }

    }

    fun logout() {
        auth.signOut()
    }

}