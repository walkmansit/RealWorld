package com.walkmansit.realworld.di


import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.walkmansit.realworld.data.repository.UserPreferencesRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//
//    private const val USER_PREFERENCES_NAME = "user_preferences"
//
//    private val Context.dataStore by preferencesDataStore(
//        name = USER_PREFERENCES_NAME
//    )
//
//    @Singleton
//    @Provides
//    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
//        return UserPreferencesRepository(dataStore = context.dataStore)
//    }
//}
