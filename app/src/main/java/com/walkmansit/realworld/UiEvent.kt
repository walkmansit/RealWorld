package com.walkmansit.realworld

sealed class UiEvent {
    data object Undefined : UiEvent()
    data class SnackbarEvent(val message: String) : UiEvent()
    data class NavigateEvent(val route: String) : UiEvent()
}