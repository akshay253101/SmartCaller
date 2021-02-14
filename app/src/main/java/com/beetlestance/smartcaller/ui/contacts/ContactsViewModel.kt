package com.beetlestance.smartcaller.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.domain.executors.AddToBlockList
import com.beetlestance.smartcaller.domain.executors.RemoveFromBlockList
import com.beetlestance.smartcaller.domain.observers.ObserveContacts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val observeContacts: ObserveContacts,
    private val addToBlockList: AddToBlockList,
    private val removeFromBlockList: RemoveFromBlockList
) : ViewModel() {

    private val pagingConfig = PagingConfig(pageSize = 20, initialLoadSize = 30)

    val contactsPagedData: Flow<PagingData<Contact>>
        get() = observeContacts.observe().cachedIn(viewModelScope)

    init {
        observeContacts(params = ObserveContacts.Params(pagingConfig = pagingConfig))
    }

    fun updateBlockStatus(contact: Contact) {
        viewModelScope.launch {
            if (contact.isBlocked) removeFromBlockList.executeSync(contact)
            else addToBlockList.executeSync(contact)
        }
    }

}