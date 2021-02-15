package com.beetlestance.smartcaller.ui.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.states.CallLog
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.executors.AddToBlockList
import com.beetlestance.smartcaller.domain.executors.RemoveFromBlockList
import com.beetlestance.smartcaller.domain.observers.ObserveBlockedContacts
import com.beetlestance.smartcaller.domain.observers.ObserveCallLogs
import com.beetlestance.smartcaller.utils.validNumberOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CallLogsViewModel @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeCallLogs: ObserveCallLogs,
    private val addToBlockList: AddToBlockList,
    private val removeFromBlockList: RemoveFromBlockList,
    private val observeBlockedContacts: ObserveBlockedContacts,
) : ViewModel() {

    private val pagingConfig = PagingConfig(pageSize = 10, initialLoadSize = 20)

    private val blockedContacts: Flow<List<BlockedContact>>
        get() = observeBlockedContacts.observe()

    val callLogPagedData: Flow<PagingData<CallLog>>
        get() = observeCallLogs.observe()
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
            .flowOn(dispatchers.io)
            .combine(blockedContacts) { pagingData, blockedContacts ->
                pagingData.map { contact ->
                    val blockedContact = blockedContacts.find { blockedContact ->
                        blockedContact.number == contact.number.validNumberOrNull()
                    }
                    contact.copy(
                        isBlocked = blockedContact != null,
                        blockedOn = blockedContact?.blockedOn
                    )
                }
            }

    init {
        observeBlockedContacts(Unit)
        observeCallLogs(params = ObserveCallLogs.Params(pagingConfig = pagingConfig))
    }

    fun updateBlockStatus(callLog: CallLog) {
        viewModelScope.launch(dispatchers.io) {
            if (callLog.isBlocked) removeFromBlockList.executeSync(callLog.number)
            else addToBlockList.executeSync(callLog.toBlockedContact())
        }
    }

}