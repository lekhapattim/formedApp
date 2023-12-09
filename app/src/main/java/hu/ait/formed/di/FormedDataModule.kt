package hu.ait.formed.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.formed.data.DanceDAO
import hu.ait.formed.data.DancersDAO
import hu.ait.formed.data.FormDAO
import hu.ait.formed.data.FormedDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideDanceDao(appDatabase: FormedDatabase): DanceDAO {
        return appDatabase.danceDAO()
    }

    @Provides
    fun provideFormDao(appDatabase: FormedDatabase): FormDAO {
        return appDatabase.formDAO()
    }

    @Provides
    fun provideDancersDao(appDatabase: FormedDatabase): DancersDAO {
        return appDatabase.dancersDAO()
    }


    @Provides
    @Singleton
    fun provideFormedDatabase(@ApplicationContext appContext: Context): FormedDatabase {
        return FormedDatabase.getDatabase(appContext)
    }
}