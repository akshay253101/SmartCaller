package com.beetlestance.smartcaller.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.beetlestance.smartcaller.data.datasource.AppTables

@Entity(tableName = AppTables.BLOCKED_CONTACTS_TABLE)
data class BlockedContact(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Long = 0,

    @ColumnInfo(name = "name") val name: String?,

    @ColumnInfo(name = "number") val number: String,

    @ColumnInfo(name = "blockedOn") val blockedOn: Long

) : SmartCallerEntity