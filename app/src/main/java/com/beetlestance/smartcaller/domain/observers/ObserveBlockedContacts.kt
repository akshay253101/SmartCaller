package com.beetlestance.smartcaller.domain.observers

import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.domain.ObserveUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveBlockedContacts @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ObserveUseCase<String, List<BlockedContact>>() {

    override fun createObservable(params: String): Flow<List<BlockedContact>> {
        return contactsRepository.observeBlockedContacts(query = "%$params%")
    }

    companion object {
        const val FETCH_ALL = ""
    }
}