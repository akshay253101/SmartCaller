package com.beetlestance.smartcaller.data.repository

import android.content.Context
import androidx.paging.PagingSource
import com.beetlestance.smartcaller.data.datasource.store.BlockedContactsStore
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.ApplicationContext
import com.beetlestance.smartcaller.utils.ContactsDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val blockedContactsStore: BlockedContactsStore,
    private val dispatchers: AppCoroutineDispatchers
) : ContactsRepository {

    override fun contactsPageSource(): PagingSource<Int, Contact> {
        return ContactsDataSource(applicationContext.contentResolver, dispatchers)
    }

    override fun observeBlockedContacts(): Flow<List<BlockedContact>> {
        return blockedContactsStore.observeContacts()
    }

    override suspend fun addToBlockedList(blockedContact: BlockedContact) {
        blockedContactsStore.addBlockedContact(blockedContact)
    }

    override suspend fun removeFromBlockedList(contactId: Int, number: String) {
        blockedContactsStore.removeBlockedContact(contactId, number)
    }
}