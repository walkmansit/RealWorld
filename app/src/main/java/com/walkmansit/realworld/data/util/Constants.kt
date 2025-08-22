package com.walkmansit.realworld.data.util

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val USER_PREFERENCES_NAME = "USER_PREFERENCES"

    object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_BIO = stringPreferencesKey("user_bio")
        val USER_IMAGE = stringPreferencesKey("user_image")
    }
}