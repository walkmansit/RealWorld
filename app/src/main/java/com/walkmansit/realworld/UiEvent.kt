package com.walkmansit.realworld

sealed class UiEvent {
    data object Undefined : UiEvent()
    data class SnackBarEvent(val message: String) : UiEvent()
    data class NavigateEvent(val route: String) : UiEvent()
}