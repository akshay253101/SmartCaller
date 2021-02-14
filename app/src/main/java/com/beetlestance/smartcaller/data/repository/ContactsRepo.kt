package com.beetlestance.smartcaller.data.repository

import android.os.Build
import androidx.annotation.RequiresApi

interface ContactsRepo {

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun syncWithSystemBlockedContacts()
}