package com.beetlestance.smartcaller.domain.executors

import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveFromBlockList @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val contactsRepository: ContactsRepository
) : UseCase<String>() {

    override suspend fun doWork(params: String) {
        return withContext(appCoroutineDispatchers.io) {
            contactsRepository.removeFromBlockedList(number = params)
        }
    }
}