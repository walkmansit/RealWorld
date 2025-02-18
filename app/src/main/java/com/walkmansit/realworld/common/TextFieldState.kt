package com.walkmansit.realworld.common

data class TextFieldState(
    val text: String = "",
    val error: String? = null,
) {
    fun hasError(): Boolean {
        return !error.isNullOrEmpty()
    }
}