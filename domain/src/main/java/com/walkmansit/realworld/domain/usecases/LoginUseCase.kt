package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.model.CommonError
import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.DispatcherProvider
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.withContext

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Either<Either<CommonError, LoginFailed>, User> {
        val emailError = if (email.isBlank()) "email cannot be blank" else null
        val passwordError = if (password.isBlank()) "password cannot be blank" else null

        return if (emailError == null && passwordError == null) {
            withContext(dispatcherProvider.io) {
                authRepository.login(UserLoginCredentials(email, password)).also {
                    if (it is Either.Success) userPreferencesRepository.updateUser(Either.success(it.value))
                }
            }
        } else {
            Either.fail(Either.success(LoginFailed(passwordError, emailError)))
        }
    }
}
