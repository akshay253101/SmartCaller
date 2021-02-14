package com.beetlestance.smartcaller.utils

import android.os.Build

fun isAtLeastVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= version
}
