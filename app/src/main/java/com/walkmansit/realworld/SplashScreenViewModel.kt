package com.walkmansit.realworld

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.walkmansit.realworld.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.map

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(){
    private val _localUser = MutableStateFlow<UserAuth>(value = UserAuth(name = "asd", token = "dfsdf"))
    val localUser: StateFlow<UserAuth> = _localUser.asStateFlow()

    val userAuthFlow : Flow<UiEvent> = userPreferencesRepository.userPreferencesFlow.map { value: UserAuth ->
        if (value.undefined) UiEvent.NavigateEvent(RwDestinations.REGISTRATION_ROUTE)
        else UiEvent.SnackbarEvent("Root")
        //if (value.undefined) UiEvents.NavigateEvent("Registration")
        //else UiEvents.NavigateEvent("Root")

    }
}

data class UserAuth(
    val name: String,
    val token: String,
    val undefined : Boolean = false
)