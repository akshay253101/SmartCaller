package com.beetlestance.smartcaller.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.executors.AddToBlockList
import com.beetlestance.smartcaller.domain.executors.RemoveFromBlockList
import com.beetlestance.smartcaller.domain.observers.ObserveBlockedContacts
import com.beetlestance.smartcaller.domain.observers.ObserveContacts
import com.beetlestance.smartcaller.utils.validNumberOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeContacts: ObserveContacts,
    private val observeBlockedContacts: ObserveBlockedContacts,
    private val addToBlockList: AddToBlockList,
    private val removeFromBlockList: RemoveFromBlockList
) : ViewModel() {

    private val pagingConfig = PagingConfig(pageSize = 10, initialLoadSize = 20)

    private val blockedContacts: Flow<List<BlockedContact>>
        get() = observeBlockedContacts.observe()

    val contactsPagedData: Flow<PagingData<Contact>>
        get() = observeContacts.observe()
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
        observeBlockedContacts(ObserveBlockedContacts.FETCH_ALL)
        observeContacts()
    }

    fun observeContacts() {
        observeContacts(params = ObserveContacts.Params(pagingConfig = pagingConfig))
    }

    fun updateBlockStatus(contact: Contact) {
        viewModelScope.launch(dispatchers.io) {
            if (contact.isBlocked) removeFromBlockList.executeSync(contact.number)
            else addToBlockList.executeSync(contact.toBlockedContact())
        }
    }

    fun executeQuery(query: String) {
        observeContacts(params = ObserveContacts.Params(query = query, pagingConfig = pagingConfig))
    }

}