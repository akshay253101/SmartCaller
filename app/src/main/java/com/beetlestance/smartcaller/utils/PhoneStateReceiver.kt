package com.beetlestance.smartcaller.utils

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beetlestance.smartcaller.di.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneStateReceiver @Inject constructor(
    @ApplicationContext private val context: Context
) : PhoneStateListener() {

    private val _callState: MutableLiveData<CallState> = MutableLiveData(CallState.IDLE)
    val callState: LiveData<CallState> = _callState

    init {
        registerCallback()
    }

    @SuppressLint("MissingPermission")
    private fun registerCallback() {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(this, LISTEN_CALL_STATE)
    }

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        validateAndResetState()

        val newState = when (state) {
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
        _callState.value = newState
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
