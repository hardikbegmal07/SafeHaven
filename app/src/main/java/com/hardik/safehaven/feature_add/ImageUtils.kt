package com.hardik.safehaven.feature_add

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


fun createImageUri(context: Context): Uri {

    val imageFile = File.createTempFile(
        "safehaven_",
        ".jpg",
        context.cacheDir // is ANDROID's temporary / private storage area for our app
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}