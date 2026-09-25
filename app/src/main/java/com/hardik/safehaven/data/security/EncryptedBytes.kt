package com.hardik.safehaven.data.security

data class EncryptedBytes(
    val cipherText: ByteArray,
    val iv: ByteArray
)