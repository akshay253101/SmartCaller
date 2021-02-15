package com.beetlestance.smartcaller.utils.extensions

import android.provider.CallLog
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.transition.TransitionManager
import com.beetlestance.smartcaller.R

@BindingAdapter("callType")
fun callType(view: AppCompatImageView, callType: Int) {
    view.apply {
        val callTypeRes = when (callType) {
            CallLog.Calls.OUTGOING_TYPE -> R.drawable.ic_baseline_call_made_24
            CallLog.Calls.INCOMING_TYPE -> R.drawable.ic_baseline_call_received_24
            else -> R.drawable.ic_baseline_call_missed_24
        }
        setImageResource(callTypeRes)
    }
}

@BindingAdapter("animateVisibility")
fun animateVisibility(view: View, isVisible: Boolean) {
    val constraintLayout = view.parent as? ViewGroup
    constraintLayout?.let { TransitionManager.beginDelayedTransition(it) }
    view.isVisible = isVisible
}