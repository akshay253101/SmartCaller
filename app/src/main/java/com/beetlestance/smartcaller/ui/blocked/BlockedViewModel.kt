package com.beetlestance.smartcaller.ui.blocked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beetlestance.smartcaller.data.entities.BlockedContact
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.domain.executors.AddToBlockList
import com.beetlestance.smartcaller.domain.executors.RemoveFromBlockList
import com.beetlestance.smartcaller.domain.observers.ObserveBlockedContacts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlockedViewModel @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val observeBlockedContacts: ObserveBlockedContacts,
    private val addToBlockList: AddToBlockList,
    private val removeFromBlockList: RemoveFromBlockList
) : ViewModel() {

    val blockedContacts: Flow<List<BlockedContact>>
        get() = observeBlockedContacts.observe()

    init {
        observeBlockedContacts(Unit)
    }

    fun updateBlockStatus(blockedContact: BlockedContact) {
        viewModelScope.launch(dispatchers.io) {
            removeFromBlockList.executeSync(blockedContact.number)
        }
    }

    fun addToBlockList(name: String, number: String) {
        viewModelScope.launch(dispatchers.io) {
            val blockedContact = BlockedContact(
                name = if (name.isBlank()) number else name,
                number = number,
                blockedOn = System.currentTimeMillis()
            )
            addToBlockList.executeSync(blockedContact)
        }
    }
}