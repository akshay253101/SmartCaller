package com.beetlestance.smartcaller.utils

internal val indianMobileNumberRegex = Regex("^[6-9]\\d{9}$")

fun String.isValidPhoneNumber(): Boolean {
    if (isEmpty()) return true
    return matches(indianMobileNumberRegex)
}

fun String.validNumberOrNull(): String? {
    val number: String = removeISDCode().replace("\\s".toRegex(), "")
    return if (number.isValidPhoneNumber()) number else null
}

private fun String.removeISDCode(): String {
    var number: String = this

    // removes space if any
    if (number.contains(" ")) number = number.replace(" ", "")

    if (number.contains("-")) number = number.replace("-", "")

    if (number.startsWith("0")) number = number.replaceFirst("0", "")

    //removes
    if (number.startsWith("+")) number = number.replace("+91", "")

    return number
}