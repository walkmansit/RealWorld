package com.walkmansit.realworld.di


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
