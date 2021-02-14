package com.beetlestance.smartcaller.data.datasource.store

import com.beetlestance.smartcaller.data.datasource.dao.BlockedContactsDao
import com.beetlestance.smartcaller.data.entities.BlockedContact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedContactsStore @Inject constructor(
    private val blockedContactsDao: BlockedContactsDao
) {

    suspend fun addBlockedContact(blockedContact: BlockedContact) {
        val blockedContacts = blockedContactsDao.findContacts(blockedContact.number)
        if (blockedContacts.isEmpty()) blockedContactsDao.insert(blockedContact)
    }

    suspend fun removeBlockedContact(number: String) {
        val blockedContacts = blockedContactsDao.findContacts(number)
        blockedContactsDao.deleteEntity(blockedContacts)
    }

    fun observeContacts() = blockedContactsDao.contactsObservable()

}