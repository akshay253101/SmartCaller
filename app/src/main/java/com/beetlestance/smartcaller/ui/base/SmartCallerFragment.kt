package com.beetlestance.smartcaller.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.beetlestance.smartcaller.utils.bindWithLifecycleOwner
import dagger.android.support.DaggerFragment

abstract class SmartCallerFragment<V : ViewDataBinding>(
    @LayoutRes layoutId: Int
) : DaggerFragment(layoutId) {

    var binding: V? = null

    fun requireBinding(): V = requireNotNull(binding)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = bindWithLifecycleOwner()
        onViewCreated(requireBinding(), savedInstanceState)
    }

    abstract fun onViewCreated(binding: V, savedInstanceState: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}