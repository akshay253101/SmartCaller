package com.beetlestance.smartcaller.domain.observers

import androidx.paging.*
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.domain.PagingUseCase
import com.beetlestance.smartcaller.utils.validNumberOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveContacts @Inject constructor(
    private val contactsRepository: ContactsRepository
) : PagingUseCase<ObserveContacts.Params, Contact>() {

    @OptIn(ExperimentalPagingApi::class)
    override fun createObservable(params: Params): Flow<PagingData<Contact>> {
        var blockedContactsCount: Int? = null
        var factory: PagingSource<Int, Contact>? = null
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                contactsRepository.contactsPageSource().also { factory = it }
            }
        ).flow.combine(contactsRepository.observeBlockedContacts()) { pagingData, blockedContacts ->
            if (blockedContactsCount == null) blockedContactsCount = blockedContacts.size

            val isBlockListUpdated = blockedContactsCount != blockedContacts.size
            if (isBlockListUpdated) {
                blockedContactsCount = blockedContacts.size
                factory?.invalidate()
            }

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
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val query: String = "%%"
    ) : Parameters<Contact>
}