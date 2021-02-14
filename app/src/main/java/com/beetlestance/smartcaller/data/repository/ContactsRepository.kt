package com.beetlestance.smartcaller.data.repository

import androidx.paging.PagingSource
import com.beetlestance.smartcaller.utils.ContactsDataSource

interface ContactsRepository {

    fun contactsPageSource(): PagingSource<Int, ContactsDataSource.Contact>
}