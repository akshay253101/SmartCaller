package com.beetlestance.smartcaller.data.repository

import android.content.Context
import android.database.Cursor
import android.provider.CallLog.Calls
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import androidx.paging.PagingSource
import com.beetlestance.smartcaller.data.datasource.store.BlockedContactsStore
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.states.CallLog
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.ApplicationContext
import com.beetlestance.smartcaller.utils.ContactsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val blockedContactsStore: BlockedContactsStore,
    private val dispatchers: AppCoroutineDispatchers
) : ContactsRepository {

    override fun contactsPageSource(query: String): PagingSource<Int, Contact> {
        return ContactsDataSource(dispatchers) { pageSize, key ->
            mapQueryToContacts(pageSize, key, query)
        }
    }

    override fun callLogsPageSource(query: String): PagingSource<Int, CallLog> {
        return ContactsDataSource(dispatchers) { pageSize, key ->
            mapQueryToCallLogs(pageSize, key, query)
        }
    }

    override fun observeBlockedContacts(query: String): Flow<List<BlockedContact>> {
        return blockedContactsStore.observeContacts(query)
    }

    override suspend fun addToBlockedList(blockedContact: BlockedContact) {
        blockedContactsStore.addBlockedContact(blockedContact)
    }

    override suspend fun removeFromBlockedList(number: String) {
        blockedContactsStore.removeBlockedContact(number)
    }

    private suspend fun mapQueryToContacts(pageSize: Int, key: Int?, query: String): List<Contact> {
        return withContext(dispatchers.io) {
            val contacts = mutableListOf<Contact>()
            var cursor: Cursor? = null
            try {

                var sort = Phone.DISPLAY_NAME + " ASC LIMIT " + pageSize
                if (key != null) sort = "$sort OFFSET $key"

                val selectionQuery = if (query.isNotBlank()) {
                    "${Phone.NUMBER} LIKE ? OR ${Phone.DISPLAY_NAME} LIKE ?"
                } else null

                val selectionArgs = if (selectionQuery != null) {
                    arrayOf("%$query%", "%$query%")
                } else null

                cursor = applicationContext.contentResolver.query(
                    Phone.CONTENT_URI,
                    arrayOf(Phone.CONTACT_ID, Phone.LOOKUP_KEY, Phone.NUMBER, Phone.DISPLAY_NAME),
                    selectionQuery,
                    selectionArgs,
                    sort
                )

                cursor?.apply {
                    moveToFirst()
                    while (!isAfterLast) {
                        val id: Int = getInt(getColumnIndex(Phone.CONTACT_ID))
                        val lookupKey: String = getString(getColumnIndex(Phone.LOOKUP_KEY))
                        val number: String = getString(getColumnIndex(Phone.NUMBER))
                        val name: String = getString(getColumnIndex(Phone.DISPLAY_NAME))
                        contacts.add(
                            Contact(id = id, name = name, number = number, lookUpKey = lookupKey)
                        )
                        moveToNext()
                    }
                    close()
                }

            } catch (e: Exception) {
                Log.e(javaClass.simpleName, e.message, e.cause)
            }

            cursor?.close()
            contacts.toList()
        }
    }

    private suspend fun mapQueryToCallLogs(pageSize: Int, key: Int?, query: String): List<CallLog> {
        return withContext(dispatchers.io) {
            val logs = mutableListOf<CallLog>()
            var cursor: Cursor? = null
            try {

                var sort = Calls.DATE + " DESC ${Calls.LIMIT_PARAM_KEY} " + pageSize
                if (key != null) sort = "$sort ${Calls.OFFSET_PARAM_KEY} $key"

                val selectionQuery = if (query.isNotBlank()) {
                    "${Calls.NUMBER} LIKE ? OR ${Calls.CACHED_NAME} LIKE ?"
                } else null

                val selectionArgs = if (selectionQuery != null) {
                    arrayOf("%$query%", "%$query%")
                } else null

                cursor = applicationContext.contentResolver.query(
                    Calls.CONTENT_URI,
                    arrayOf(Calls.CACHED_NAME, Calls.TYPE, Calls.NUMBER),
                    selectionQuery,
                    selectionArgs,
                    sort
                )

                cursor?.apply {
                    moveToFirst()
                    while (!isAfterLast) {
                        val number: String = getString(getColumnIndex(Calls.NUMBER))
                        val name: String? = getString(getColumnIndex(Calls.CACHED_NAME))
                        val callType: Int = getInt(getColumnIndex(Calls.TYPE))
                        logs.add(
                            CallLog(name = name ?: number, number = number, callType = callType)
                        )
                        moveToNext()
                    }
                    close()
                }

            } catch (e: Exception) {
                Log.e(javaClass.simpleName, e.message, e.cause)
            }

            cursor?.close()
            logs.toList()
        }
    }
}