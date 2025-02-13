package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.util.Either
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) : Either<LoginFailed, User> {
        val emailError = if (email.isBlank()) "email cannot be blank" else null
        val passwordError = if (password.isBlank()) "password cannot be blank" else null

        return if (emailError == null && passwordError == null){
            repository.login(UserLoginCredentials(email, password))
        }
        else Either.fail(LoginFailed(emailError, passwordError))
    }
}