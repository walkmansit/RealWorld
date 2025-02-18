package com.walkmansit.realworld.domain.use_case

import com.walkmansit.realworld.data.repository.UserPreferencesRepositoryImpl
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: UserPreferencesRepositoryImpl
) {
}