package com.beetlestance.smartcaller.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.beetlestance.smartcaller.domain.observers.ObserveContacts
import com.beetlestance.smartcaller.utils.ContactsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val observeContacts: ObserveContacts
) : ViewModel() {

    private val pagingConfig = PagingConfig(pageSize = 20, initialLoadSize = 30)

    val contactsPagedData: Flow<PagingData<ContactsDataSource.Contact>>
        get() = observeContacts.observe()

    init {
        observeContacts(params = ObserveContacts.Params(pagingConfig = pagingConfig))
    }

}