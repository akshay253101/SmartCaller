package com.beetlestance.smartcaller.utils.callmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import com.android.internal.telephony.ITelephony
import com.beetlestance.smartcaller.data.datasource.store.BlockedContactsStore
import com.beetlestance.smartcaller.di.ApplicationContext
import com.beetlestance.smartcaller.utils.isAtLeastVersion
import com.beetlestance.smartcaller.utils.testNotification
import com.beetlestance.smartcaller.utils.validNumberOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallStateManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val store: BlockedContactsStore
) : PhoneStateListener() {

    private val _callState: MutableStateFlow<CallState?> = MutableStateFlow(CallState.IDLE)
    val callState: Flow<CallState> = _callState.filterNotNull().distinctUntilChanged()

    private val telephonyManager =
        applicationContext.getSystemService(Context.TELECOM_SERVICE) as TelecomManager


    init {
        registerCallback()
    }

    @SuppressLint("MissingPermission")
    private fun registerCallback() {
        val telephony = applicationContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        _callState.value = telephonyStateToCallState(telephony.callState)
        telephony.listen(this, LISTEN_CALL_STATE)
    }

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        val number = phoneNumber?.validNumberOrNull()
        validateAndResetState()

        val newState = telephonyStateToCallState(state)
        if (number != null && newState == CallState.INCOMING_CALL_RECEIVED) {
            checkAndBlockCaller(number)
        }
        _callState.value = newState
    }

    private fun telephonyStateToCallState(state: Int): CallState {
        return when (state) {
            TelephonyManager.CALL_STATE_RINGING -> CallState.INCOMING_CALL_RECEIVED
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                if (isAtLeast(CallState.INCOMING_CALL_RECEIVED)) CallState.INCOMING_CALL_PICKED
                else CallState.OUTGOING_CALL_STARTED
            }
            TelephonyManager.CALL_STATE_IDLE ->
                when {
                    isAtLeast(CallState.INCOMING_CALL_PICKED) -> CallState.INCOMING_CALL_ENDED
                    isAtLeast(CallState.INCOMING_CALL_RECEIVED) -> CallState.MISSED_CALL
                    isAtLeast(CallState.OUTGOING_CALL_STARTED) -> CallState.OUTGOING_CALL_ENDED
                    else -> CallState.IDLE
                }
            else -> throw IllegalStateException("Unknown call state - $state")
        }
    }

    private fun validateAndResetState() {
        val state = requireNotNull(_callState.value)
        if (state == CallState.MISSED_CALL || state == CallState.OUTGOING_CALL_ENDED
            || state == CallState.INCOMING_CALL_ENDED
        ) _callState.value = CallState.IDLE
    }

    private fun isAtLeast(state: CallState): Boolean {
        val currentState = requireNotNull(_callState.value)
        return currentState >= state
    }

    private fun checkAndBlockCaller(number: String) {
        if (store.isNumberBlocked(number)) rejectCall(number)
    }

    @SuppressLint("MissingPermission")
    private fun rejectCall(number: String) {
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
        applicationContext.testNotification(number = number)
    }

    enum class CallState {
        IDLE,
        OUTGOING_CALL_STARTED,
        OUTGOING_CALL_ENDED,
        INCOMING_CALL_RECEIVED,
        INCOMING_CALL_PICKED,
        INCOMING_CALL_ENDED,
        MISSED_CALL,
    }
}
