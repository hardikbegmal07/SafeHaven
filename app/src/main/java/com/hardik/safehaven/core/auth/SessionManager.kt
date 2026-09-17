package com.hardik.safehaven.core.auth

import android.content.Context
import androidx.core.content.edit

class SessionManager(private val context: Context) {

    private val prefs = context.getSharedPreferences(
        "safehaven_session",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val SESSION_EXPIRY = "session_expiry"
    }

    fun saveLogin(expiryTime: Long) {
        prefs.edit {
            putBoolean(IS_LOGGED_IN, true)
                .putLong(SESSION_EXPIRY, expiryTime)
        }
    }

    fun isSessionValid(): Boolean {
        val isLoggedIn = prefs.getBoolean(IS_LOGGED_IN, false)
        val expiryTime = prefs.getLong(SESSION_EXPIRY, 0L)

        return isLoggedIn && System.currentTimeMillis() < expiryTime
    }

    fun logout() {
        prefs.edit { clear() }
    }

}