package com.hardik.safehaven.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

// We are building a local encryption layer for SAFEHAVEN
//  goal : even if someone copies our Room DB, they cannot read user data.

class CryptoManager {

    companion object {
        private const val KEYSTORE = "AndroidKeyStore" // tells Android to use the System-Managed secure KEYSTORE
        // this keystore may be hardware-backed.
        // It prevents key export and protects key even if device is rooted.
        // It performs crypto inside secure hardware
        private const val KEY_ALIAS = "SAFEHAVEN_ASS_KEY"
        //  It is just a label (name of the key inside the Android keystore), it is not the key itself.
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
    } // It is like STATIC block of code which holds CONSTANTS, STATIC MEMBERS, FACTORY METHODS
    //  these values do not change and belong to the class which should be recreated per instance
    // CONFIGURATION CONSTANTS

    init {
        generateKeyIfNeeded()
    } // inti block runs when the class instance is created ...
    // e.g. val crypto = CryptoManager() // it automatically ensures key exists,
    // thus preventing Null key usage and Manual key setup mistakes

    private fun generateKeyIfNeeded() {

        val keyStore = KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        } // gets Android system keystore

        if (!keyStore.containsAlias(KEY_ALIAS)) { // checks that if key does not exists, create it ...
            // prevents regenerating key and losing ability to decrypt old data.

            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE
            ) // generate AES key inside AndroidKeyStore provider.

            val keySpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM) // why GCM mode chosen ??
                //  Because:
                //   AES-CBC → Confidentiality only
                //   AES-GCM → Confidentiality + Integrity
                //  Integrity = tamper detection.
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_NONE
                ) // GCM does not need Padding.
                .setKeySize(256)  // 256-bit AES = strong symmetric encryption.
                .setRandomizedEncryptionRequired(true)  // ensures IV must be random
                .build()

            // this key can only:
            //  Encrypt and Decrypt
            // and nothing else

            keyGenerator.init(keySpec)
            keyGenerator.generateKey()
        }
    }
    // Create AES key only once ...

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        }

        return keyStore.getKey(KEY_ALIAS, null) as SecretKey // returns SecretKey
        // But:
        //  we cannot extract raw key bytes.
        //  we cannot log it.
        //  we cannot export it.
        //  operations happen internally.
    }

    fun encrypt(plainText: String): EncryptedData {
        val cipher = Cipher.getInstance(TRANSFORMATION) // creates cipher engine

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getSecretKey()
        ) // initialize cipher using keystore key.

        val iv = cipher.iv // initialization vector ?? why it is needed ?
        //  without IV: same input -> same output
        //  with IV: same input -> different ciphertext
        // this improves security
        val encryptedBytes = cipher.doFinal(
            plainText.toByteArray(Charsets.UTF_8)
        ) // performs AES encryption + GCM authentication tag generation

        return EncryptedData(
            cipherText = Base64.encodeToString(
                encryptedBytes,
                Base64.DEFAULT
            ),
            iv = Base64.encodeToString(
                iv,
                Base64.DEFAULT
            )
        )
        // why Base64 ??
        //  Encrypted data is raw bytes.
        //  Room database cannot store raw byte arrays easily.
        // so, we convert to Base64 string.
    }

    fun decrypt(
        cipherText: String,
        iv: String
    ): String {

        val cipher = Cipher.getInstance(TRANSFORMATION)

        val spec = GCMParameterSpec(
            128,
            Base64.decode(iv, Base64.DEFAULT)
        ) // why 128 in GCMParameterSpec ??
        //  128 = Authentication tag length in bits
        //  GCM adds authentication tag

        cipher.init(
            Cipher.DECRYPT_MODE,
            getSecretKey(),
            spec
        )

        val decryptedBytes = cipher.doFinal(
            Base64.decode(cipherText, Base64.DEFAULT)
        )

        return String(decryptedBytes, Charsets.UTF_8)
    }
    // in decrypt() method
    //  we have created cipher
    //  created GCMParameterSpec with IV (initialization vector)
    //  initialize cipher with key + IV
    //  decrypt
}

// CryptoManager is a local encryption engine for SafeHaven.
// Its job is to:
//  Encrypt sensitive data before storing it in Room.
//  Decrypt it when reading
//  Ensure confidentiality + integrity


//       UI -> UseCase -> Repository -> CryptoManager -> Storage

// CryptoManager class provides
//  secure AES key generation
//  secure key storage (Android Keystore)
//  Encryption using AES-GCM
//  Decryption using integrity validation

