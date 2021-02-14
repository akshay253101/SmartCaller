package com.beetlestance.smartcaller.data.datasource

import android.content.Context
import android.os.Build
import android.provider.BlockedNumberContract.BlockedNumbers
import androidx.annotation.RequiresApi
import com.beetlestance.smartcaller.di.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class ContactsDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun fetchBlockedNumbers() {
        return suspendCancellableCoroutine { cont ->
            val cursor = applicationContext.contentResolver.query(
                BlockedNumbers.CONTENT_URI, arrayOf(
                    BlockedNumbers.COLUMN_ID,
                    BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
                    BlockedNumbers.COLUMN_E164_NUMBER
                ), null, null, null
            )

            if (cursor == null) {
                cont.resumeWithException(IllegalStateException("Blocked Contact Cursor is Null"))
            } else {
                if (cursor.count > 0) {
                    cont.resume(Unit) {
                        cursor.close()
                    }
                }

                cont.invokeOnCancellation {
                    cursor.close()
                }
            }
        }
    }
}