package com.beetlestance.smartcaller.utils

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import androidx.paging.PagingSource
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import kotlinx.coroutines.withContext

class ContactsDataSource(
    private val contentResolver: ContentResolver,
    private val dispatcher: AppCoroutineDispatchers
) : PagingSource<Int, Contact>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contact> {
        val data = mapQueryToContacts(params.loadSize, params.key)
        return withContext(dispatcher.io) {
            LoadResult.Page(
                data = data,
                prevKey = guessPrevKey(params, data),
                nextKey = guessNextKey(params, data)
            )
        }
    }

    private fun guessPrevKey(params: LoadParams<Int>, data: List<Contact>): Int? {
        return when (params) {
            is LoadParams.Refresh -> null
            is LoadParams.Prepend -> if (data.isEmpty()) null else params.key - data.size
            is LoadParams.Append -> params.key
        }
    }

    private fun guessNextKey(params: LoadParams<Int>, data: List<Contact>): Int? {
        return when (params) {
            is LoadParams.Refresh -> params.loadSize
            is LoadParams.Prepend -> params.key
            is LoadParams.Append -> if (data.isEmpty()) null else params.key + data.size
        }
    }

    private suspend fun mapQueryToContacts(pageSize: Int, key: Int?): List<Contact> {
        return withContext(dispatcher.io) {
            val contacts = mutableListOf<Contact>()
            var cursor: Cursor? = null
            try {

                var sort = Phone.DISPLAY_NAME + " ASC LIMIT " + pageSize
                if (key != null) sort = "$sort OFFSET $key"

                cursor = contentResolver.query(
                    Phone.CONTENT_URI,
                    arrayOf(
                        Phone.CONTACT_ID,
                        Phone.LOOKUP_KEY,
                        Phone.NUMBER,
                        Phone.DISPLAY_NAME
                    ),
                    null,
                    null,
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

}