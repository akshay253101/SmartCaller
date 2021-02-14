package com.beetlestance.smartcaller.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beetlestance.smartcaller.data.datasource.dao.ContactsDao
import com.beetlestance.smartcaller.data.entities.Contact

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}

internal object AppTables {

    const val CONTACTS_TABLE: String = "contacts_table"

}