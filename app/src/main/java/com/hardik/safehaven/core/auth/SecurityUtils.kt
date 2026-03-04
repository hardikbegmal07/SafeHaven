package com.hardik.safehaven.core.auth

import android.content.Context
import android.content.pm.ApplicationInfo
import java.io.File

object SecurityUtils {

    fun isDebuggable(context: Context): Boolean {
        return context.applicationInfo.flags and
                ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    fun isDeviceRooted(): Boolean {
        val paths = listOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su"
        )
        return paths.any { File(it).exists() }
    }
}