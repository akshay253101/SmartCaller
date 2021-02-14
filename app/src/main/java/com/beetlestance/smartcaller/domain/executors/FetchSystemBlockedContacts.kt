package com.beetlestance.smartcaller.domain.executors

import android.os.Build
import androidx.annotation.RequiresApi
import com.beetlestance.smartcaller.data.repository.ContactsRepo
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class FetchSystemBlockedContacts @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val contactsRepo: ContactsRepo
) : UseCase<Unit>() {

    override suspend fun doWork(params: Unit) {
        return withContext(appCoroutineDispatchers.io) {
            contactsRepo.syncWithSystemBlockedContacts()
        }
    }
}