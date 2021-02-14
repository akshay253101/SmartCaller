package com.beetlestance.smartcaller.domain.executors

import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchSystemBlockedContacts @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val contactsRepository: ContactsRepository
) : UseCase<Unit>() {

    override suspend fun doWork(params: Unit) {
        return withContext(appCoroutineDispatchers.io) {

        }
    }
}