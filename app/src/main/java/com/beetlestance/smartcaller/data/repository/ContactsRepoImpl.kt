package com.beetlestance.smartcaller.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject

class ContactsRepoImpl @Inject constructor(

) : ContactsRepo {

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun syncWithSystemBlockedContacts() {

    }
}