package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.model.RequestFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {

    suspend operator fun invoke(): Either<Either<EmptyUser, RequestFailed>, User> {
        when (val savedUser = userPreferencesRepository.userFlow.first()) {
            is Either.Fail -> {
                return Either.fail(savedUser)
            }

            is Either.Success -> {
                val profileResp = authRepository.getProfile(savedUser.value.username)

                return when (profileResp) {
                    is Either.Fail -> Either.fail(Either.Success(profileResp.value))
                    is Either.Success -> savedUser
                }
            }
        }
    }
}