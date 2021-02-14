package com.beetlestance.smartcaller.utils.extensions

import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageTint")
fun tintColorName(view: AppCompatImageView, color: Int) {
    view.apply {
        imageTintList = ColorStateList.valueOf(color)
    }
}