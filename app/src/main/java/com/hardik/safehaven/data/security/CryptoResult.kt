package com.hardik.safehaven.data.security

sealed class CryptoResult<out T> {
    data class Success<T>(val data: T) : CryptoResult<T>()
    data class Error(val exception: Exception) : CryptoResult<Nothing>()
}
// it is 'data-layer' ABSTRACTION ...