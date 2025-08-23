package com.walkmansit.realworld.presenter.components

data class TextFieldState(
    val text: String = "",
    val error: String? = null,
) {
    fun hasError(): Boolean = !error.isNullOrEmpty()
}
