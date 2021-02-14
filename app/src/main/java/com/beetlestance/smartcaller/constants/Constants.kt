package com.beetlestance.smartcaller.constants

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.beetlestance.smartcaller.utils.isAtLeastVersion

object SmartCallerRequiredPermissions {

    const val CALL_PHONE: String = Manifest.permission.CALL_PHONE
    const val READ_CALL_LOGS: String = Manifest.permission.READ_CALL_LOG
    const val READ_PHONE_STATE: String = Manifest.permission.READ_PHONE_STATE

    @RequiresApi(Build.VERSION_CODES.O)
    const val ANSWER_PHONE_CALLS: String = Manifest.permission.ANSWER_PHONE_CALLS

    val permissions: List<String> =
        mutableListOf(CALL_PHONE, READ_CALL_LOGS, READ_PHONE_STATE).apply {
            if (isAtLeastVersion(Build.VERSION_CODES.O)) add(ANSWER_PHONE_CALLS)
        }

    const val REQUEST_CODE: Int = 1001
}