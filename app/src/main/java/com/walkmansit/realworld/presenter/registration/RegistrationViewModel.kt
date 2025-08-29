package com.walkmansit.realworld.presenter.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkmansit.realworld.BuildConfig
import com.walkmansit.realworld.domain.model.CommonError
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.usecases.RegistrationUseCase
import com.walkmansit.realworld.domain.util.Either
import com.walkmansit.realworld.presenter.components.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.api.ActionShareBehavior
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.dsl.lazyStore
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.dsl.updateStateOrThrow
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import javax.inject.Inject

sealed interface RegistrationIntent : MVIIntent {
    data class UpdateUserName(
        val username: String,
    ) : RegistrationIntent

    data class UpdateEmail(
        val email: String,
    ) : RegistrationIntent

    data class UpdatePassword(
        val password: String,
    ) : RegistrationIntent

    data object Submit : RegistrationIntent

    data class SubmitComplete(
        val result: Either<Either<CommonError, RegistrationFailed>, User>,
    ) : RegistrationIntent

    data object RedirectLogin : RegistrationIntent
}

sealed interface RegistrationAction : MVIAction {
    data object RedirectLogin : RegistrationAction

    data class RedirectFeed(
        val username: String,
    ) : RegistrationAction

    data class ShowMessage(
        val text: String,
    ) : RegistrationAction
}

data class RegistrationFields(
    val username: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
)

sealed interface RegistrationState : MVIState {
    data object Loading : RegistrationState

    data class Error(
        val message: String,
    ) : RegistrationState

    data class Content(
        val fields: RegistrationFields = RegistrationFields(),
    ) : RegistrationState

    data class LoadingOnSubmit(
        val fields: RegistrationFields = RegistrationFields(),
    ) : RegistrationState
}

private typealias Ctx = PipelineContext<RegistrationState, RegistrationIntent, RegistrationAction>

@HiltViewModel
class RegistrationViewModel
@Inject
constructor(
    private val registrationUseCase: RegistrationUseCase,
) : ViewModel(),
    Container<RegistrationState, RegistrationIntent, RegistrationAction> {
    override val store by lazyStore(initial = RegistrationState.Content(), scope = viewModelScope) {

            configure {
                name = "RegistrationStore"
                debuggable = BuildConfig.DEBUG
                actionShareBehavior = ActionShareBehavior.Distribute()
                parallelIntents = true
            }

            recover { e: Exception ->
                updateState {
                    RegistrationState.Error(e.message ?: "Unknown error")
                }
                null
            }

            reduce { intent ->
                when (intent) {
                    is RegistrationIntent.UpdateUserName -> updateUsername(intent.username)
                    is RegistrationIntent.UpdateEmail -> updateEmail(intent.email)
                    is RegistrationIntent.UpdatePassword -> updatePassword(intent.password)
                    is RegistrationIntent.Submit -> submit()
                    is RegistrationIntent.SubmitComplete -> submitComplete(intent.result)
                    is RegistrationIntent.RedirectLogin -> action(RegistrationAction.RedirectLogin)
                }
            }
        }

    private suspend fun Ctx.updateUsername(newUsername: String) {
        updateStateOrThrow<RegistrationState.Content, _> {
            copy(
                fields =
                    with(fields) {
                        copy(username = username.copy(text = newUsername))
                    },
            )
        }
    }

    private suspend fun Ctx.updateEmail(newEmail: String) {
        updateStateOrThrow<RegistrationState.Content, _> {
            copy(
                fields =
                    with(fields) {
                        copy(email = email.copy(text = newEmail))
                    },
            )
        }
    }

    private suspend fun Ctx.updatePassword(newPassword: String) {
        updateStateOrThrow<RegistrationState.Content, _> {
            copy(
                fields =
                    with(fields) {
                        copy(password = password.copy(text = newPassword))
                    },
            )
        }
    }

    private suspend fun Ctx.submitComplete(result: Either<Either<CommonError, RegistrationFailed>, User>) {
        updateStateOrThrow<RegistrationState.LoadingOnSubmit, _> {
            when (result) {
                is Either.Fail -> {
                    val fail = result.value
                    when (fail) {
                        is Either.Fail -> {
                            action(RegistrationAction.ShowMessage(fail.value.error))
                            RegistrationState.Content(fields)
                        }

                        is Either.Success -> {
                            with(fields) {
                                val newFields =
                                    copy(
                                        username = username.copy(error = fail.value.usernameError),
                                        email = email.copy(error = fail.value.emailError),
                                        password = password.copy(error = fail.value.passwordError),
                                    )
                                RegistrationState.Content(newFields)
                            }
                        }
                    }
                }

                is Either.Success -> {
                    action(RegistrationAction.RedirectFeed(result.value.username))
                    RegistrationState.Content()
                }
            }
        }
    }

    private suspend fun Ctx.submit() {
        updateStateOrThrow<RegistrationState.Content, _> {
            viewModelScope.launch {
                val regUserResponse =
                    registrationUseCase(
                        fields.username.text,
                        fields.email.text,
                        fields.password.text,
                    )
                intent(RegistrationIntent.SubmitComplete(regUserResponse))
            }

            RegistrationState.LoadingOnSubmit(fields)
        }
    }
}
