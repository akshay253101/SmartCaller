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
    @Query(value = ALL_CONTACTS)
    abstract fun contactsObservable(): Flow<List<BlockedContact>>

    @Transaction
    @Query(value = "SELECT * FROM ${AppTables.BLOCKED_CONTACTS_TABLE} WHERE contact_id LIKE :contactId AND number LIKE :number")
    abstract fun findContacts(contactId: Int, number: String): List<BlockedContact>

    companion object {
        private const val ALL_CONTACTS = "SELECT * FROM ${AppTables.BLOCKED_CONTACTS_TABLE}"
    }
}
