package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.CommonError
import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(): Boolean {
        return withContext(Dispatchers.IO) {
            userPreferencesRepository.updateUser(Either.fail(EmptyUser))
            true
        }
    }
}