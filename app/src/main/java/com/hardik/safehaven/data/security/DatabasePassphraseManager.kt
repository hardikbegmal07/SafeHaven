package com.hardik.safehaven.data.security

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import java.security.SecureRandom

// we will:
//  A) generate random 32-byte passphrase (a long secure, authentication method
//      composed of sequence of random words, if offers higher security and easier memorability
//      than traditional passwords)
//  B) Encrypt using crypto manager
//  C) store encrypted passphrase in shared preferences
//  D) decrypt on startup
//  E) Supply to SQLCipher

class DatabasePassphraseManager(
    context: Context,
    private val cryptoManager: CryptoManager
) {
    companion object {
        private const val PREF_NAME = "safehaven_secure_prefs"
        private const val KEY_PASSPHRASE = "encrypted_db_passphrase"
        private const val KEY_IV = "encrypted_db_passphrase_iv"
        private const val PASSPHRASE_LENGTH = 32
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getOrCreatePassphrase() : ByteArray {
        val storedCipher = prefs.getString(KEY_PASSPHRASE, null)
        val storedIv = prefs.getString(KEY_IV, null)

        return if(storedCipher != null && storedIv != null) {
            when(val result = cryptoManager.decryptSafe(storedCipher, storedIv)) {
                is CryptoResult.Success -> {
                    Base64.decode(result.data, Base64.DEFAULT)
                }
                is CryptoResult.Error -> {
                    throw IllegalArgumentException("Failed to decrypt BD passphrase", result.exception)
                }
            }
        } else {
            generateAndStorePassphrase()
        }
    }

    private fun generateAndStorePassphrase(): ByteArray {
        val random = SecureRandom()
        val passphrase = ByteArray(PASSPHRASE_LENGTH)
        random.nextBytes(passphrase)

        val base64Pass = Base64.encodeToString(passphrase, Base64.DEFAULT)

        when (val result = cryptoManager.encryptSafe(base64Pass)) {
            is CryptoResult.Success -> {
                prefs.edit {
                    putString(KEY_PASSPHRASE, result.data.cipherText)
                    putString(KEY_IV, result.data.iv)
                }
            }

            is CryptoResult.Error -> {
                throw IllegalStateException("Failed to encrypt DB passphrase", result.exception)
            }
        }

        return passphrase
    }

}