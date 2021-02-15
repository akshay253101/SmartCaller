package com.beetlestance.smartcaller.domain.observers

import androidx.paging.*
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.data.states.CallLog
import com.beetlestance.smartcaller.domain.PagingUseCase
import com.beetlestance.smartcaller.utils.validNumberOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveCallLogs @Inject constructor(
    private val contactsRepository: ContactsRepository
) : PagingUseCase<ObserveCallLogs.Params, CallLog>() {

    @OptIn(ExperimentalPagingApi::class)
    override fun createObservable(params: Params): Flow<PagingData<CallLog>> {
        var blockedContactsCount: Int? = null
        var factory: PagingSource<Int, CallLog>? = null
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                contactsRepository.callLogsPageSource().also { factory = it }
            }
        ).flow.combine(contactsRepository.observeBlockedContacts()) { pagingData, blockedContacts ->
            if (blockedContactsCount == null) blockedContactsCount = blockedContacts.size

            val isBlockListUpdated = blockedContactsCount != blockedContacts.size
            if (isBlockListUpdated) {
                blockedContactsCount = blockedContacts.size
                factory?.invalidate()
            }

            pagingData.mapSync { contact ->
                val blockedContact = blockedContacts.find { blockedContact ->
                    blockedContact.number == contact.number.validNumberOrNull()
                }
                contact.copy(
                    isBlocked = blockedContact != null,
                    blockedOn = blockedContact?.blockedOn
                )
            }
        }
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val query: String = "%%"
    ) : Parameters<CallLog>
}