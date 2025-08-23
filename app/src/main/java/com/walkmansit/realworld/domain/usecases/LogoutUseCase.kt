package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutUseCase
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(): Boolean =
            withContext(Dispatchers.IO) {
                userPreferencesRepository.updateUser(Either.fail(EmptyUser))
                true
            }
    }
