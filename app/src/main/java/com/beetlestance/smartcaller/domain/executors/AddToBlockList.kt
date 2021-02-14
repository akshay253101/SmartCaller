package com.beetlestance.smartcaller.domain.executors

import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddToBlockList @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val contactsRepository: ContactsRepository
) : UseCase<Contact>() {

    override suspend fun doWork(params: Contact) {
        return withContext(appCoroutineDispatchers.io) {
            contactsRepository.addToBlockedList(
                blockedContact = BlockedContact(
                    name = params.name,
                    number = params.number,
                    contactId = params.id,
                    blockedOn = System.currentTimeMillis()
                )
            )
        }
    }
}