package com.beetlestance.smartcaller.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beetlestance.smartcaller.data.datasource.dao.BlockedContactsDao
import com.beetlestance.smartcaller.data.entities.BlockedContact

@Database(entities = [BlockedContact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blockedContactsDao(): BlockedContactsDao
}

internal object AppTables {

    const val BLOCKED_CONTACTS_TABLE: String = "blocked_contacts_table"

}