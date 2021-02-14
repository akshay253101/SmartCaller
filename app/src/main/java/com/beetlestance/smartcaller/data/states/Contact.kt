package com.beetlestance.smartcaller.data.states

data class Contact(

    val id: Int,

    val name: String,

    val number: String,

    val lookUpKey: String,

    val isBlocked: Boolean = false,

    val blockedOn: Long? = null,
)