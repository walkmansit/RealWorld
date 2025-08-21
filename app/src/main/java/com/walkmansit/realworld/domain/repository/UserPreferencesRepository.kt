package com.walkmansit.realworld.domain.repository

import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userFlow: Flow<Either<EmptyUser, User>>
    suspend fun updateUser(user: Either<EmptyUser, User>)
}