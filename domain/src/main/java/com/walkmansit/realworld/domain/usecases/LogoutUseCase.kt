package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.DispatcherProvider
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.withContext

class LogoutUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(): Boolean =
        withContext(dispatcherProvider.io) {
            userPreferencesRepository.updateUser(Either.fail(EmptyUser))
            true
        }
}
