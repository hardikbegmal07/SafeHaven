package com.hardik.safehaven.data.security

import android.content.Context
import android.net.Uri

fun Uri.toByteArray(context: Context): ByteArray? {

    return context.contentResolver.openInputStream(this)?.use {
        inputStream -> inputStream.readBytes()
    }

}