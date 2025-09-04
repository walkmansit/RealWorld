package com.walkmansit.realworld.domain.usecases

import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.model.RequestFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.DispatcherProvider
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CheckAuthUseCase(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    suspend operator fun invoke(): Either<Either<EmptyUser, RequestFailed>, User> {
        return  when (val savedUser = userPreferencesRepository.userFlow.first()) {
            is Either.Fail -> {
                 Either.fail(savedUser)
            }

            is Either.Success -> {
                 withContext(dispatcherProvider.io) {
                    val profileResp = authRepository.getProfile(savedUser.value.username)

                    when (profileResp) {
                        is Either.Fail -> Either.fail(Either.Success(profileResp.value))
                        is Either.Success -> savedUser
                    }
                }
            }
        }
    }
}
