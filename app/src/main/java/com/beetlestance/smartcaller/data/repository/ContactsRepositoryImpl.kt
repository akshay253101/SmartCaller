package com.beetlestance.smartcaller.data.repository

import android.content.Context
import androidx.paging.PagingSource
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.ApplicationContext
import com.beetlestance.smartcaller.utils.ContactsDataSource
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val dispatchers: AppCoroutineDispatchers
) : ContactsRepository {

    override fun contactsPageSource(): PagingSource<Int, ContactsDataSource.Contact> {
        return ContactsDataSource(applicationContext.contentResolver, dispatchers)
    }
}