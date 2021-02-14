package com.beetlestance.smartcaller.utils

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout

class SmartCallerMotionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.progress = progress
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            progress = state.progress
            when {
                state.progress < 0.5f && state.progress != 0f -> transitionToStart()
                state.progress > 0.5f && state.progress != 1f -> transitionToEnd()
            }
            super.onRestoreInstanceState(state.superState)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    class SavedState : BaseSavedState {

        var progress: Float = 0f

        constructor(source: Parcelable?) : super(source)

        constructor(source: Parcel) : super(source) {
            progress = source.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(progress)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel) = SavedState(source)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }
}
