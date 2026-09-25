package com.hardik.safehaven.domain.model

data class SecureItem(
    val id: String,
    val title: String,
    val content: String,
    val type: String,
    val createdAt: Long,
    val imageData: ByteArray? = null,
    val mimeType: String? = null
)