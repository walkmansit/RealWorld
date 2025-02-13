package com.walkmansit.realworld.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.repository.AuthRepositoryImpl
import com.walkmansit.realworld.data.repository.UserPreferencesRepository
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.use_case.RegistrationUseCase
import com.walkmansit.realworld.domain.util.Constants
import com.walkmansit.realworld.domain.util.Constants.BASE_URL
import com.walkmansit.realworld.ui.registration.RegistrationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesPreferenceDataStore(@ApplicationContext context: Context) : DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Constants.USER_PREFERENCES_NAME)
            }
        )


    @Provides
    @Singleton
    fun providesUserPreferencesRepository(dataStore: DataStore<Preferences>) =
        UserPreferencesRepository(dataStore)

    @Provides
    @Singleton
    fun providesApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(apiService: ApiService) : AuthRepository = AuthRepositoryImpl(apiService)

//    @Provides
//    @Singleton
//    fun providesRegistrationUseCase(repository: AuthRepository) = RegistrationUseCase(repository)

//    @Provides
//    @Singleton
//    fun providesRegistrationViewModel(authRepository: AuthRepositoryImpl: ApiService) = RegistrationViewModel(apiService)
}