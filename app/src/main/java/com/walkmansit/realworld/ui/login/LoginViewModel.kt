package com.walkmansit.realworld.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.common.TextFieldState
import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.use_case.LoginUseCase
import com.walkmansit.realworld.domain.util.Either
import dagger.hilt.android.lifecycle.HiltViewModel
import pro.respawn.flowmvi.api.ActionShareBehavior
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.updateStateOrThrow
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import javax.inject.Inject

data class LoginFields(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
)

sealed interface LoginIntent : MVIIntent {
    data class UpdateEmail(val email: String) : LoginIntent
    data class UpdatePassword(val password: String) : LoginIntent
    data object SubmitStart : LoginIntent
    data class SubmitComplete(val result: Either<LoginFailed, User>) : LoginIntent
    data object RedirectRegistration : LoginIntent
}

sealed interface LoginState : MVIState {
    data object Loading : LoginState
    data class Error(val message: String) : LoginState
    data class Content(val fields: LoginFields = LoginFields()) : LoginState
    data class LoadingOnSubmit(val fields: LoginFields = LoginFields()) : LoginState
}

sealed class LoginAction : MVIAction {
    data class RedirectFeed(val username: String) : LoginAction()
    data object RedirectRegistration : LoginAction()
}

private typealias Ctx = PipelineContext<LoginState, LoginIntent, LoginAction>


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel(), Container<LoginState,LoginIntent, LoginAction> {

    override val store = store(initial = LoginState.Content(), scope = viewModelScope){

        configure {
            name = "LoginStore"
            debuggable = true
            actionShareBehavior = ActionShareBehavior.Distribute()
            parallelIntents = true
        }

        recover { e: Exception ->
            updateState {
                LoginState.Error(e.message ?: "Unknown error")
            }
            null
        }

        reduce { intent ->
            when (intent) {
                is LoginIntent.UpdateEmail -> updateEmail(intent.email)
                is LoginIntent.UpdatePassword -> updatePassword(intent.password)
                is LoginIntent.RedirectRegistration -> action(LoginAction.RedirectRegistration)
                is LoginIntent.SubmitStart -> submitStart()
                is LoginIntent.SubmitComplete -> submitComplete(intent.result)
            }
        }
    }

    private suspend fun Ctx.updateEmail(newEmail: String){
        updateStateOrThrow<LoginState.Content, _> {
            copy(fields = with(fields){
                copy(email = email.copy(text = newEmail))
            })
        }
    }

    private suspend fun Ctx.updatePassword(newPassword: String){
        updateStateOrThrow<LoginState.Content, _> {
            copy(fields = with(fields){
                copy(password = password.copy(text = newPassword))
            })
        }
    }

    private suspend fun Ctx.submitComplete(result: Either<LoginFailed, User>){
        updateStateOrThrow<LoginState.LoadingOnSubmit, _> {
            when (result) {
                is Either.Fail -> {
                    with(fields){
                        val newFields = copy(
                            email = email.copy(error = result.value.emailError),
                            password = password.copy(error = result.value.passwordError),
                        )
                        LoginState.Content(newFields)
                    }
                }
                is Either.Success -> {
                    action(LoginAction.RedirectFeed(result.value.username))
                    LoginState.Content()
                }
            }

        }
    }

    private suspend fun Ctx.submitStart(){
        updateStateOrThrow<LoginState.Content, _> {
            val loginResult = loginUseCase(
                email = fields.email.text,
                password = fields.password.text
            )
            intent(LoginIntent.SubmitComplete(loginResult))

            LoginState.LoadingOnSubmit()
        }
    }
}