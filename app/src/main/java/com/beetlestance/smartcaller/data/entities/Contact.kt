package com.beetlestance.smartcaller.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.beetlestance.smartcaller.data.datasource.AppTables

@Entity(tableName = AppTables.CONTACTS_TABLE)
data class Contact(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Long = 0,

    @ColumnInfo(name = "index") val index: String,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "uri") val uri: String,

    @ColumnInfo(name = "number") val number: String,

    @ColumnInfo(name = "isBlocked") val isBlocked: Boolean,

    @ColumnInfo(name = "blockedOn") val blockedOn: Long

) : SmartCallerEntity