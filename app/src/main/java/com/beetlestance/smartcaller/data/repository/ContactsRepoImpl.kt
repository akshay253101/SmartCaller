package com.beetlestance.smartcaller.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.beetlestance.smartcaller.data.datasource.ContactsDataSource
import javax.inject.Inject

class ContactsRepoImpl @Inject constructor(
    private val dataSource: ContactsDataSource
) : ContactsRepo {

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun syncWithSystemBlockedContacts() {
        dataSource.fetchBlockedNumbers()
    }
}