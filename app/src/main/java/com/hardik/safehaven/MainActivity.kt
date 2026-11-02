package com.hardik.safehaven

import android.app.AlertDialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.hardik.safehaven.core.auth.SecurityUtils
import com.hardik.safehaven.core.auth.SessionManager
import com.hardik.safehaven.core.theme.SafeHavenTheme
import com.hardik.safehaven.feature_auth.AuthViewModel
import com.hardik.safehaven.feature_login.LoginViewModel
import com.hardik.safehaven.feature_auth.auth.AuthState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private var lockJob: Job? = null // when should the app lock bcz the user is inactive ?
    private var appBackgroundTime: Long? = null // how long has the app been in the background ?

    override fun onStart() {
        super.onStart()

        val backgroundTime = appBackgroundTime ?: return

        val currentTime = System.currentTimeMillis()
        val timeInBackground = currentTime - backgroundTime

        val LOCK_TIMEOUT = 90_000L // 90 sec

        if (timeInBackground > LOCK_TIMEOUT) {
            lifecycleScope.launch {
                delay(300)
                authViewModel.lock()
            }
        }

        appBackgroundTime = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (SecurityUtils.isDeviceRooted()) {
            showSecurityErrorAndExit("Rooted device detected")
            return
        }

        if (SecurityUtils.isDebuggable(this)) {

            AlertDialog.Builder(this)
                .setTitle("Security Warning")
                .setMessage(
                    "This application is running in debug mode. " +
                            "Security protections may be reduced."
                )
                .setPositiveButton("Continue", null)
                .show()
        }

         // Prevent screenshots & screen recording
         window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authState.collect { state ->
                    if (state is AuthState.Unlocked) {
                        startAutoLockTimer()
                    } else {
                        lockJob?.cancel()
                    }
                }
            }
        }

        setContent {
            SafeHavenTheme {
                SafeHeavenApp(authViewModel, activity = this)
            }
        }
    }

    private fun showSecurityErrorAndExit(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun startAutoLockTimer() {

        // If already locked, do nothing
        if (authViewModel.authState.value is AuthState.Locked) {
            lockJob?.cancel()
            return
        }

        lockJob?.cancel()

        lockJob = lifecycleScope.launch {
            delay(180_000) // extended this auto lock to 5 mins ...
            //authViewModel.lock()

            if (authViewModel.authState.value is AuthState.Unlocked) {
                authViewModel.lock()

            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (authViewModel.authState.value is AuthState.Unlocked) {
            startAutoLockTimer()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onStop() {
        super.onStop()

         appBackgroundTime = System.currentTimeMillis()
        // why onStop ?
        // BECAUSE
        //  - app fully hidden
        //  - more stable lifecycle event
        //  - avoids transient UI interruptions
    }

}