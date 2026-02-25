package com.hardik.safehaven

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hardik.safehaven.core.theme.SafeHavenTheme

// Week3
//  Rule of this week:
//  -> UI never talks to Room
//  -> viewModels can never talk to room
//  -> Room never knows about compose
//  -> Domain models (data classes that represent core business entities) never depend on ROOM

class MainActivity : ComponentActivity() { // Component Activity - is a base class that
    // supports lifecycle (lifecycle handling)
    // supports viewModels (viewModel stores)
    // supports Jetpack compose (saved state, compose support)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafeHavenTheme {
                SafeHeavenApp()
            }
        }
    }
} // Android OS needs a lifecycle owner (Activity, Fragment)
// MainActivity extends ComponentActivity
// ComponentActivity implements LifecycleOwner
// need something to attach to UI and handle rotation, background, kill, recreate

// Android Architecture answers one question:
//  Where should i put my code, so that the app doesn't become garbage ??

// 3 Main Layers of Android Architecture are
//  UI components -> ViewModel -> Repository -> Data Source (Room / API / files)

// we are done with adding info and storing it ROOM DB
// encrypted the DB so that even if it gets compromised no one can access anything
// will be adding Biometric functionality to our application