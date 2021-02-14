package com.beetlestance.smartcaller.utils.callmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.TELECOM_SERVICE
import android.os.Build
import android.telecom.TelecomManager
import android.util.Log
import com.android.internal.telephony.ITelephony
import com.beetlestance.smartcaller.data.datasource.store.BlockedContactsStore
import com.beetlestance.smartcaller.di.ApplicationContext
import com.beetlestance.smartcaller.utils.isAtLeastVersion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncomingCallManager @Inject constructor(
    @ApplicationContext applicationContext: Context,
    callStateListener: CallStateListener,
    private val store: BlockedContactsStore
) : CallStateListener.OnInComingCallReceived {

    private val telephonyManager =
        applicationContext.getSystemService(TELECOM_SERVICE) as TelecomManager

    init {
        callStateListener.attachListener(listener = this)
    }

    override fun onCallReceived(number: String) {
        checkAndBlockCaller(number)
    }

    private fun checkAndBlockCaller(number: String) {
        if (store.isNumberBlocked(number)) rejectCall()
    }

    @SuppressLint("MissingPermission")
    private fun rejectCall() {
        if (isAtLeastVersion(Build.VERSION_CODES.P)) telephonyManager.endCall()
        else {
            try {
                val method = telephonyManager.javaClass.getDeclaredMethod("getITelephony")
                method.isAccessible = true

                val telephony = method.invoke(telephonyManager) as ITelephony
                telephony.endCall()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Couldn't end call with TelephonyManager", e)
            }
        }
    }

}
