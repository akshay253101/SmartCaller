package com.beetlestance.smartcaller.data.datasource.store

import com.beetlestance.smartcaller.data.datasource.dao.BlockedContactsDao
import com.beetlestance.smartcaller.data.entities.BlockedContact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsStore @Inject constructor(
    private val blockedContactsDao: BlockedContactsDao
) {

    suspend fun saveContacts(blockedContacts: List<BlockedContact>) = blockedContactsDao.insertAll(blockedContacts)

    fun contactsPageSource(query: String) = blockedContactsDao.pagingSource(query)

}