package com.beetlestance.smartcaller.domain.observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.data.states.CallLog
import com.beetlestance.smartcaller.domain.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCallLogs @Inject constructor(
    private val contactsRepository: ContactsRepository
) : PagingUseCase<ObserveCallLogs.Params, CallLog>() {

    @OptIn(ExperimentalPagingApi::class)
    override fun createObservable(params: Params): Flow<PagingData<CallLog>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = { contactsRepository.callLogsPageSource() }
        ).flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val query: String = "%%"
    ) : Parameters<CallLog>
}