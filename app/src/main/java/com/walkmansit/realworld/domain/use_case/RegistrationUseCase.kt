package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.Either
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Either<RegistrationFailed, User> {

        val usernameError = if (username.isBlank()) "Username cannot be blank" else null
        val emailError = if (email.isBlank()) "Email cannot be blank" else null
        //val passwordError = if (password.isBlank()) "Password cannot be blank" else null
        val passwordError: String? = null

        return if (usernameError == null && emailError == null && passwordError == null) {
            repository.register(UserRegisterCredentials(username, email, password)).also {
                if (it is Either.Success) userPreferencesRepository.updateUser(it.value)
            }
        } else Either.fail(
            RegistrationFailed(
                usernameError = usernameError,
                emailError = emailError,
                passwordError = passwordError,
            )
        )

    }
}