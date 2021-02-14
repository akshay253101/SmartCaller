package com.beetlestance.smartcaller.domain.executors

import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddToBlockList @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val contactsRepository: ContactsRepository
) : UseCase<BlockedContact>() {

    override suspend fun doWork(params: BlockedContact) {
        return withContext(appCoroutineDispatchers.io) {
            contactsRepository.addToBlockedList(blockedContact = params)
        }
    }
}