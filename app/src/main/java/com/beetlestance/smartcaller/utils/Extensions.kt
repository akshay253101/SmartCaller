package com.beetlestance.smartcaller.utils

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun isAtLeastVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= version
}

fun AppCompatActivity.hasPermissions(permission: String): Boolean {
    return if (isAtLeastVersion(Build.VERSION_CODES.M)) {
        checkSelfPermission(permission)
    } else {
        ContextCompat.checkSelfPermission(this, permission)
    } == PackageManager.PERMISSION_GRANTED
}

fun AppCompatActivity.shouldShowPermissionRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}