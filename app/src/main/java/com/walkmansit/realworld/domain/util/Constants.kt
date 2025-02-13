package com.walkmansit.realworld.domain.util

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object Constants {
    const val BASE_URL = "http://10.0.2.2:8080/api/"
    const val USER_PREFERENCES_NAME = "USER_PREFERENCES"

    object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_TOKEN = stringPreferencesKey("user_token")
    }
}