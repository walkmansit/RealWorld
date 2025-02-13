package com.walkmansit.realworld.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.walkmansit.realworld.UserAuth
import com.walkmansit.realworld.domain.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    val dataStore: DataStore<Preferences>
) {
    private val TAG: String = "UserPreferencesRepo"



    val userPreferencesFlow: Flow<UserAuth> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserAuth(preferences)
            // Get our show completed value, defaulting to false if not set:

        }

    private fun mapUserAuth(preferences: androidx.datastore.preferences.core.Preferences): UserAuth {
        val name = preferences[Constants.PreferencesKeys.USER_NAME]?: "name"
        val token = preferences[Constants.PreferencesKeys.USER_TOKEN]?: "token"

        return UserAuth(name, token, preferences[Constants.PreferencesKeys.USER_TOKEN] == null)

    }

    suspend fun updateUserAuth(userAuth: UserAuth) {
        dataStore.edit { preferences ->
            preferences[Constants.PreferencesKeys.USER_NAME] = userAuth.name
            preferences[Constants.PreferencesKeys.USER_TOKEN] = userAuth.token
        }
    }
}