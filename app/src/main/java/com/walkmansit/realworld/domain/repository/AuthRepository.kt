package com.walkmansit.realworld.domain.repository

import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.util.Either

interface AuthRepository {
    suspend fun login(userCredentials: UserLoginCredentials) : Either<LoginFailed, User>
    suspend fun register(userCredentials: UserRegisterCredentials) : Either<RegistrationFailed, User>
}