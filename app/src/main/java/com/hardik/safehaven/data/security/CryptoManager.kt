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
        private const val KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "SAFEHAVEN_ASS_KEY"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
    }

    init {
        generateKeyIfNeeded()
    }

    private fun generateKeyIfNeeded() {

        val keyStore = KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        }

        if (!keyStore.containsAlias(KEY_ALIAS)) {

            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE
            )

            val keySpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_NONE
                )
                .setKeySize(256)
                .setRandomizedEncryptionRequired(true)
                .build()

            keyGenerator.init(keySpec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE).apply {
            load(null)
        }

        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    fun encrypt(plainText: String): EncryptedData {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getSecretKey()
        )

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(
            plainText.toByteArray(Charsets.UTF_8)
        )

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
    }

    fun decrypt(
        cipherText: String,
        iv: String
    ): String {

        val cipher = Cipher.getInstance(TRANSFORMATION)

        val spec = GCMParameterSpec(
            128,
            Base64.decode(iv, Base64.DEFAULT)
        )

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
}