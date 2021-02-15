package com.beetlestance.smartcaller.data.datasource.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.beetlestance.smartcaller.data.datasource.AppTables
import com.beetlestance.smartcaller.data.entities.BlockedContact
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BlockedContactsDao : EntityDao<BlockedContact>() {

    @Transaction
    @Query(value = "SELECT * FROM ${AppTables.BLOCKED_CONTACTS_TABLE} WHERE number LIKE :query OR name LIKE :query")
    abstract fun contactsObservable(query: String): Flow<List<BlockedContact>>

    @Transaction
    @Query(value = "SELECT * FROM ${AppTables.BLOCKED_CONTACTS_TABLE} WHERE number LIKE :number")
    abstract fun findContacts(number: String): List<BlockedContact>

}
