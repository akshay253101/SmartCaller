package com.beetlestance.smartcaller.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.beetlestance.smartcaller.R

fun isAtLeastVersion(version: Int): Boolean {
    return Build.VERSION.SDK_INT >= version
}

inline fun <T : ViewDataBinding> Fragment.bindWithLifecycleOwner(
    bind: (T.() -> Unit) = {}
): T {
    val binding: T = checkNotNull(DataBindingUtil.bind(requireView()))
    binding.lifecycleOwner = viewLifecycleOwner
    binding.bind()
    return binding
}

inline fun <T : ViewDataBinding> bindWithLayout(
    @LayoutRes layoutId: Int,
    parent: ViewGroup,
    attachToRoot: Boolean = false,
    bind: (T.() -> Unit) = {}
): T {
    val inflater: LayoutInflater = LayoutInflater.from(parent.context)
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, parent, attachToRoot)
    binding.bind()
    return binding
}

fun Activity.showSoftInput(view: View) {
    val imm: InputMethodManager? = getSystemService()
    val currentFocus = currentFocus
    if (currentFocus != null && imm != null) {
        imm.showSoftInput(view, 0)
    }
}