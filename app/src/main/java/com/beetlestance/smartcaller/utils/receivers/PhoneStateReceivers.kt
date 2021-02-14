package com.beetlestance.smartcaller.utils.receivers

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.beetlestance.smartcaller.utils.callmanager.IncomingCallManager
import dagger.android.DaggerBroadcastReceiver
import javax.inject.Inject

class PhoneStateReceivers : DaggerBroadcastReceiver() {

    @Inject
    lateinit var incomingCallManager: IncomingCallManager // fake Inject

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED == intent?.action) incomingCallManager
    }
}