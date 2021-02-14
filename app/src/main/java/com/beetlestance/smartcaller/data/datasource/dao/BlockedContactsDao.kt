package com.beetlestance.smartcaller.data.datasource.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.beetlestance.smartcaller.data.datasource.AppTables
import com.beetlestance.smartcaller.data.entities.BlockedContact
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BlockedContactsDao : EntityDao<BlockedContact>() {

    @Transaction
    @Query(value = ALL_CONTACTS)
    abstract fun contactsObservable(): Flow<List<BlockedContact>>

    @Query(value = "SELECT * FROM ${AppTables.BLOCKED_CONTACTS_TABLE} WHERE name LIKE :query")
    abstract fun pagingSource(query: String): PagingSource<Int, BlockedContact>

    companion object {
        private const val ALL_CONTACTS = "SELECT * FROM ${AppTables.BLOCKED_CONTACTS_TABLE}"
    }
}
