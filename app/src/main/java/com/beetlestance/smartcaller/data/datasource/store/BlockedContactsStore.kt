package com.beetlestance.smartcaller.data.datasource.store

import com.beetlestance.smartcaller.data.datasource.dao.BlockedContactsDao
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.utils.validNumberOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockedContactsStore @Inject constructor(
    private val blockedContactsDao: BlockedContactsDao
) {

    fun isNumberBlocked(phoneNumber: String): Boolean {
        val number = phoneNumber.validNumberOrNull() ?: return false
        return blockedContactsDao.findContacts(number).isNotEmpty()
    }

    suspend fun addBlockedContact(blockedContact: BlockedContact) {
        val number = blockedContact.number.validNumberOrNull() ?: return
        val blockedContacts = blockedContactsDao.findContacts(number)
        if (blockedContacts.isEmpty()) blockedContactsDao.insert(blockedContact.copy(number = number))
    }

    suspend fun removeBlockedContact(phoneNumber: String) {
        val number = phoneNumber.validNumberOrNull() ?: return
        val blockedContacts = blockedContactsDao.findContacts(number)
        blockedContactsDao.deleteEntity(blockedContacts)
    }

    fun observeContacts(query: String) = blockedContactsDao.contactsObservable(query)

}