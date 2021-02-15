package com.beetlestance.smartcaller.data.repository

import androidx.paging.PagingSource
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.states.CallLog
import com.beetlestance.smartcaller.data.states.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    fun contactsPageSource(): PagingSource<Int, Contact>

    fun callLogsPageSource(): PagingSource<Int, CallLog>

    fun observeBlockedContacts(query: String): Flow<List<BlockedContact>>

    suspend fun addToBlockedList(blockedContact: BlockedContact)

    suspend fun removeFromBlockedList(number: String)
}