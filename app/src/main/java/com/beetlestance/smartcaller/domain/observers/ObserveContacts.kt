package com.beetlestance.smartcaller.domain.observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.data.states.Contact
import com.beetlestance.smartcaller.domain.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveContacts @Inject constructor(
    private val contactsRepository: ContactsRepository
) : PagingUseCase<ObserveContacts.Params, Contact>() {

    @OptIn(ExperimentalPagingApi::class)
    override fun createObservable(params: Params): Flow<PagingData<Contact>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                contactsRepository.contactsPageSource()
            }
        ).flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val query: String = "%%"
    ) : Parameters<Contact>
}