package com.hardik.safehaven.core.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/*

    Android Authentication Stack:
    (I)   BiometricManager -> checks capability
            A) Check if the device used have the fingerprint sensor ?
            B) is fingerprint enrolled ?
            C) is device credentials enabled ?
            ## Is there a lock on the door ?

    (II)  BiometricPrompt -> UI system dialog
            This is the POPUP android shows :
             Use fingerprint to unlock
            we DO NOT build this UI ourself, "Android OS" controls it

            Because if apps made their own fingerprint UI:
            → Hackers could fake it.
            → Phishing attack possible.
            So Android keeps this at system level.

    (III) CryptoObject (Imp) -> ties authentication to cryptographic operations
            ❌ Normal (Insecure) Way
            Step 1: User authenticates with fingerprint.
            Step 2: we manually use our AES key.
            ex - if (fingerprintSuccess) {
                    decryptData()
                 }
           Problem ??
            If attacker bypasses UI logic (using Frida / hooking):
            They can call decryptData() directly.
            UI and cryptography are separate, And that is DANGEROUS

            ✅ Secure Way (CryptoObject)
            Instead of: Authenticate → then use key
            We do,      Authenticate → system unlocks key → crypto operation allowed

            Meaning, The key itself is locked by Android.
            If fingerprint is not successful,
             Cipher.init() FAILS, Even if attacker bypasses UI.

    (IV)  Android Keystore -> holds key

    STRONG vs WEAK Biometrics
    BiometricManager.Authenticators.BIOMETRIC_STRONG = fingerprint, 3D face
    BiometricManager.Authenticators.BIOMETRIC_WEAK = basic face recognition

    CryptoObject ??
    Instead of going this way,
           Authenticate user -> then manually use key
    we do, Authenticate user ->   system unlocks key   -> crypto operation proceeds


    this prevents replay attacks (A replay attack in Android is a security vulnerability
        where an attacker intercepts a legitimate communication (such as an authentication token,
        encrypted login credentials, or a command) between an Android app and a server, and
        later re-sends ("replays") that exact data to the server)
*/

class BiometricHelper(
    private val activity: FragmentActivity
) { // we pass the Activity bcz BiometricPrompt requires a FragmentActivity,
    // BiometricPrompt attaches to the activity and survives configuration changes properly.

    fun canAuthenticate(): Int {
        val biometricManager = BiometricManager.from(activity)

        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
    }
    // It asks android:
    //  Does device have strong biometric ? BIOMETRIC_SUCCESS
    //  Is it enrolled ?                    BIOMETRIC_ERROR_NO_HARDWARE
    //  Or at least device PIN enabled ?    BIOMETRIC_ERROR_NONE_ENROLLED

    fun showBiometricPrompt(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val executor = ContextCompat.getMainExecutor(activity)
        // Biometric callbacks must run on main thread.
        // This ensures:
        //  UI updates are safe
        //  No threading crash

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object: BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess() // If fingerprint or PIN succeeds -> app will be unlocked
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errString.toString())
                    // If:
                    //  User cancels
                    //  Too many attempts
                    //  Hardware failure, we call OnError(
                }
            }
        ) // This creates the system authentication controller.
        // It needs:
        //  Activity (lifecycle owner)
        //  Executor (main thread)
        //  Callback (what to do after authentication)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock SafeHaven")
            .setSubtitle("Authenticate to access secure vault")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
        // This defines Title, Subtitle, Allowed authenticators
        // This configures the system dialog.

        biometricPrompt.authenticate(promptInfo)
        // This:
        //  Shows system dialog
        //  Waits for fingerprint/PIN
        //  Triggers callback
    }
}