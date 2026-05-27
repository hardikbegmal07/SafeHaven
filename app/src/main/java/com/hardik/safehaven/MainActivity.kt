package com.hardik.safehaven

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hardik.safehaven.core.auth.SecurityUtils
import com.hardik.safehaven.core.theme.SafeHavenTheme
import com.hardik.safehaven.presentation.AuthViewModel
import com.hardik.safehaven.presentation.auth.AuthState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.sqlcipher.BuildConfig

// Week3
//  Rule of this week:
//  -> UI never talks to Room
//  -> viewModels can never talk to room
//  -> Room never knows about compose
//  -> Domain models (data classes that represent core business entities) never depend on ROOM

// we are going to do this way :
// App goes background
//     ↓
// Start grace timer
//     ↓
// If user returns quickly → stay unlocked
// If timeout exceeded → lock app

class MainActivity : FragmentActivity() { // Component Activity - is a base class that
    // supports lifecycle (lifecycle handling)
    // supports viewModels (viewModel stores)
    // supports Jetpack compose (saved state, compose support)

    private val authViewModel: AuthViewModel by viewModels()
    private var lockJob: Job? = null
    private var appBackgroundTime: Long = 0L

    override fun onStart() {
        super.onStart()

        val currentTime = System.currentTimeMillis()
        val timeInBackground = currentTime - appBackgroundTime

        val LOCK_TIMEOUT = 30_000L // 30sec

        if (timeInBackground > LOCK_TIMEOUT) {
            lifecycleScope.launch {
                delay(300)
                authViewModel.lock()
            } // lock() happens BEFORE collectors active
        }
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

//        // If already locked, do nothing
//        if (authViewModel.authState.value is AuthState.Locked) {
//            lockJob?.cancel()
//            return
//        }

        lockJob?.cancel()

        lockJob = lifecycleScope.launch {
            delay(60_000) // extended this auto lock to 5mins ...
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

} // Android OS needs a lifecycle owner (Activity, Fragment)
// MainActivity extends ComponentActivity
// ComponentActivity implements LifecycleOwner
// need something to attach to UI and handle rotation, background, kill, recreate

// Android Architecture answers one question:
//  Where should i put my code, so that the app doesn't become garbage ??

// 3 Main Layers of Android Architecture are
//  UI components -> ViewModel -> Repository -> Data Source (Room / API / files)

// Data is encrypted AND access is strictly controlled at runtime.

/*
  Optimized flow for our app -

  App Launch
      ↓
  LockScreen
      ↓
  Biometric Success
      ↓
  Home Screen

*/

// we are now going to implement:
//  Lock after 60 seconds inactivity
//  Lock on background
//  Lock on process death