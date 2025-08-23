package com.walkmansit.realworld.data.util

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val USER_PREFERENCES_NAME = "USER_PREFERENCES"

    object PreferencesKeys {
        val userNameKey = stringPreferencesKey("userName")
        val userTokenKey = stringPreferencesKey("userToken")
        val userEmailKey = stringPreferencesKey("userEmail")
        val userBioKey = stringPreferencesKey("userBio")
        val userImageKey = stringPreferencesKey("userImage")
    }
}
