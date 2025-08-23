package com.walkmansit.realworld.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.walkmansit.realworld.data.util.Constants.PreferencesKeys.userTokenKey
import com.walkmansit.realworld.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first

class TokenRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : TokenRepository {
    override suspend fun getToken(): String {
        val pref = dataStore.data.first()
        return pref[userTokenKey] ?: ""
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[userTokenKey] = token
        }
    }

    override suspend fun deleteToken() {
        dataStore.edit { preferences ->
            preferences.remove(userTokenKey)
        }
    }
}
