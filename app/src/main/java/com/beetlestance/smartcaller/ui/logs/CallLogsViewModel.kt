package com.beetlestance.smartcaller.ui.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.beetlestance.smartcaller.data.states.CallLog
import com.beetlestance.smartcaller.domain.executors.AddToBlockList
import com.beetlestance.smartcaller.domain.executors.RemoveFromBlockList
import com.beetlestance.smartcaller.domain.observers.ObserveCallLogs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CallLogsViewModel @Inject constructor(
    private val observeCallLogs: ObserveCallLogs,
    private val addToBlockList: AddToBlockList,
    private val removeFromBlockList: RemoveFromBlockList
) : ViewModel() {

    private val pagingConfig = PagingConfig(pageSize = 20, initialLoadSize = 30)

    val callLogPagedData: Flow<PagingData<CallLog>>
        get() = observeCallLogs.observe().cachedIn(viewModelScope)

    init {
        observeCallLogs(params = ObserveCallLogs.Params(pagingConfig = pagingConfig))
    }

    fun updateBlockStatus(callLog: CallLog) {
        viewModelScope.launch {
            if (callLog.isBlocked) removeFromBlockList.executeSync(callLog.number)
            else addToBlockList.executeSync(callLog.toBlockedContact())
        }
    }

}