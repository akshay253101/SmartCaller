package com.beetlestance.smartcaller.data.states

import com.beetlestance.smartcaller.data.entities.BlockedContact

data class Contact(

    val id: Int,

    val name: String,

    val number: String,

    val lookUpKey: String,

    val isBlocked: Boolean = false,

    val blockedOn: Long? = null,
) {
    fun toBlockedContact() = BlockedContact(
        id = 0,
        name = name,
        number = number,
        blockedOn = System.currentTimeMillis()
    )
}