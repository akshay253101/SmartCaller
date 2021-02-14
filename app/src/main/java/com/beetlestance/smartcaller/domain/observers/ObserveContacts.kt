package com.beetlestance.smartcaller.domain.observers

import androidx.paging.*
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.domain.PagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveContacts @Inject constructor(
    private val contactsRepository: ContactsRepository
) : PagingUseCase<ObserveContacts.Params, Contact>() {

    @OptIn(ExperimentalPagingApi::class)
    override fun createObservable(params: Params): Flow<PagingData<Contact>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = { contactsRepository.contactsPageSource() }
        ).flow.combine(contactsRepository.observeBlockedContacts()) { pagingData, blockedContacts ->

            pagingData.map { contact ->
                val blockedContact = blockedContacts.find { blockedContact ->
                    blockedContact.contactId == contact.id && blockedContact.number == contact.number
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