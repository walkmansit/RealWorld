package com.walkmansit.realworld.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.walkmansit.realworld.data.api.ApiService
import com.walkmansit.realworld.data.repository.ArticleRepositoryImpl
import com.walkmansit.realworld.data.repository.AuthRepositoryImpl
import com.walkmansit.realworld.data.repository.TokenRepositoryImpl
import com.walkmansit.realworld.data.repository.UserPreferencesRepositoryImpl
import com.walkmansit.realworld.data.util.Constants
import com.walkmansit.realworld.domain.repository.ArticleRepository
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.repository.TokenRepository
import com.walkmansit.realworld.domain.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providesPreferenceDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Constants.USER_PREFERENCES_NAME)
            },
        )

    @Provides
    @Singleton
    fun providesUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository =
        UserPreferencesRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun providesTokenRepository(dataStore: DataStore<Preferences>): TokenRepository = TokenRepositoryImpl(dataStore)

    @Provides
    @Singleton
    fun providesAuthRepository(apiService: ApiService): AuthRepository = AuthRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun providesArticleRepository(apiService: ApiService): ArticleRepository {
        val articleRepositoryImpl = ArticleRepositoryImpl(apiService)
        return articleRepositoryImpl
    }
}