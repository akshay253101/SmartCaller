package com.beetlestance.smartcaller.ui

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetlestance.smartcaller.domain.executors.FetchSystemBlockedContacts
import com.beetlestance.smartcaller.utils.isAtLeastVersion
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel() {

    init {
        if (isAtLeastVersion(Build.VERSION_CODES.N)) {
            viewModelScope.launch {

            }
        }
    }

}