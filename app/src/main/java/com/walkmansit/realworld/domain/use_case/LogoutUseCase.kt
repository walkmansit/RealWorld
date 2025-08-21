package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.CommonError
import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.delay
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(): Boolean {
        userPreferencesRepository.updateUser(Either.fail(EmptyUser))
        return true
    }
}