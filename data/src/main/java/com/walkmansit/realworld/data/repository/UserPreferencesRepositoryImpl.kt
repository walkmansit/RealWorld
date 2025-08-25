package com.walkmansit.realworld.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.walkmansit.realworld.data.util.Constants
import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesRepository {
    override val userFlow: Flow<Either<EmptyUser, User>>
        get() =
            dataStore.data
                .catch { exception ->
                    // dataStore.data throws an IOException when an error is encountered when reading data
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        emit(emptyPreferences())
                    }
                }.map { preferences ->
                    mapUserAuth(preferences)
                    // Get our show completed value, defaulting to false if not set:
                }

    private fun mapUserAuth(preferences: Preferences): Either<EmptyUser, User> {
        val token = preferences[Constants.PreferencesKeys.userTokenKey] ?: ""
        if (token.isEmpty()) {
            return Either.fail(EmptyUser)
        }

        val name = preferences[Constants.PreferencesKeys.userNameKey] ?: "name"
        val email = preferences[Constants.PreferencesKeys.userEmailKey] ?: ""
        val bio = preferences[Constants.PreferencesKeys.userBioKey] ?: ""
        val image = preferences[Constants.PreferencesKeys.userImageKey] ?: ""

        return Either.success(User(email, token, name, bio, image))
    }

    override suspend fun updateUser(user: Either<EmptyUser, User>) {
        when (user) {
            is Either.Fail -> {
                dataStore.edit { preferences ->
                    preferences[Constants.PreferencesKeys.userTokenKey] = ""
                }
            }

            is Either.Success -> {
                dataStore.edit { preferences ->
                    preferences[Constants.PreferencesKeys.userNameKey] = user.value.username
                    preferences[Constants.PreferencesKeys.userTokenKey] = user.value.token
                    preferences[Constants.PreferencesKeys.userEmailKey] = user.value.email
                    preferences[Constants.PreferencesKeys.userBioKey] = user.value.bio
                    preferences[Constants.PreferencesKeys.userImageKey] = user.value.image ?: ""
                }
            }
        }
    }
}
