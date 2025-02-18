package com.walkmansit.realworld.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.walkmansit.realworld.domain.repository.TokenRepository
import com.walkmansit.realworld.domain.util.Constants.PreferencesKeys.USER_TOKEN
import kotlinx.coroutines.flow.first

class TokenRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : TokenRepository {
    override suspend fun getToken(): String {
        val pref = dataStore.data.first()
        return pref[USER_TOKEN] ?: ""
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    override suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
        }
    }
}