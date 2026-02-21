package com.hardik.safehaven.data.security

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import java.security.SecureRandom

// SQLCipher needs a database passphrase:
//  without it, the database CANNOT be opened and the file is UNREADABLE.

// Problem : Where do we store that passphrase securely ??
//  We CANNOT hardcode it or store it in simple shared preferences or store it in a constant or derive it from a week string
//  that is why we have created a secure passphrase lifecycle manager.

// we will:
//  A) generate random 32-byte passphrase (a long secure, authentication method
//      composed of sequence of random words, if offers higher security and easier memorability
//      than traditional passwords)
//  B) Encrypt using crypto manager(Android keystore - AES)
//  C) store encrypted passphrase in shared preferences
//  D) decrypt it on app startup
//  E) Supply it to SQLCipher

// It ensures that the SQLCipher password is NEVER stored in plaintext.

class DatabasePassphraseManager(
    context: Context,
    private val cryptoManager: CryptoManager
) {
    companion object {
        private const val PREF_NAME = "safehaven_secure_prefs"
        private const val KEY_PASSPHRASE = "encrypted_db_passphrase"
        private const val KEY_IV = "encrypted_db_passphrase_iv"
        private const val PASSPHRASE_LENGTH = 32 // 32 bytes = 256 bits.
        // SQLCipher uses AES-256.
        // So that we generate a cryptographically strong key.
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    // We store:
    //  encrypted passphrase
    //  its IV

    fun getOrCreatePassphrase() : ByteArray {
        val storedCipher = prefs.getString(KEY_PASSPHRASE, null)
        val storedIv = prefs.getString(KEY_IV, null)

        // checks if the encrypted key already exists,
        //  if YES decrypt it, it No generate one ...
        return if(storedCipher != null && storedIv != null) {
            when(val result = cryptoManager.decryptSafe(storedCipher, storedIv)) {
                is CryptoResult.Success -> {
                    Base64.decode(result.data, Base64.DEFAULT)
                } // recover's the original 32 byte key
                is CryptoResult.Error -> {
                    throw IllegalArgumentException("Failed to decrypt BD passphrase", result.exception)
                }
            }
        } else {
            generateAndStorePassphrase()
        }
    } // This ensures that Passphrase is generated only once.
    // It stays consistent across app launches and DB can always be reopened.

    private fun generateAndStorePassphrase(): ByteArray {
        val random = SecureRandom()
        val passphrase = ByteArray(PASSPHRASE_LENGTH)
        random.nextBytes(passphrase)
        // SecureRandom:
        //  Cryptographically secure
        //  Not predictable
        //  Not seeded weakly

        val base64Pass = Base64.encodeToString(passphrase, Base64.DEFAULT)
        // because AES encrypt method expects String input.

        when (val result = cryptoManager.encryptSafe(base64Pass)) {
            is CryptoResult.Success -> {
                prefs.edit {
                    putString(KEY_PASSPHRASE, result.data.cipherText)
                    putString(KEY_IV, result.data.iv)
                }
                // Attacker dumping storage will see:
                //  Random encrypted string
                //  IV (initialization vector)
            }

            is CryptoResult.Error -> {
                throw IllegalStateException("Failed to encrypt DB passphrase", result.exception)
            }
        } // Our CryptoManager uses:
          //  Android Keystore
          //  Hardware-backed AES
          //  Non-exportable key
          // The DB passphrase is now protected by hardware-backed encryption.

        return passphrase
    }
    // this is where real security happens ...
    //

}

// Where It Fits in Architecture
//     Android Keystore
//            ↓
//    CryptoManager (AES-GCM)
//            ↓
//    DatabasePassphraseManager
//            ↓
//    SQLCipher SupportFactory
//            ↓
//    Encrypted Room DB

