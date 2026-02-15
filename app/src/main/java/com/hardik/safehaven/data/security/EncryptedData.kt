package com.hardik.safehaven.data.security

data class EncryptedData(
    val cipherText: String,
    val iv: String
)
