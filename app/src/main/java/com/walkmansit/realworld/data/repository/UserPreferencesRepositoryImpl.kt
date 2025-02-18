package com.walkmansit.realworld.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.walkmansit.realworld.domain.model.EmptyUser
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.util.Constants
import com.walkmansit.realworld.domain.util.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override val userFlow: Flow<Either<EmptyUser, User>>
        get() = dataStore.data
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
        val token =
            preferences[Constants.PreferencesKeys.USER_TOKEN] ?: return Either.fail(EmptyUser)

        val name = preferences[Constants.PreferencesKeys.USER_NAME] ?: "name"
        val email = preferences[Constants.PreferencesKeys.USER_EMAIL] ?: ""
        val bio = preferences[Constants.PreferencesKeys.USER_BIO] ?: ""
        val image = preferences[Constants.PreferencesKeys.USER_IMAGE] ?: ""

        return Either.success(User(email, token, name, bio, image))
    }

    override suspend fun updateUser(user: User) {
        dataStore.edit { preferences ->
            preferences[Constants.PreferencesKeys.USER_NAME] = user.username
            preferences[Constants.PreferencesKeys.USER_TOKEN] = user.token
            preferences[Constants.PreferencesKeys.USER_EMAIL] = user.email
            preferences[Constants.PreferencesKeys.USER_BIO] = user.bio
            preferences[Constants.PreferencesKeys.USER_IMAGE] = user.image ?: ""
        }
    }
}