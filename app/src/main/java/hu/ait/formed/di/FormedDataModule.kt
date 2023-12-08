package hu.ait.formed.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.formed.data.FormedDAO
import hu.ait.formed.data.FormedDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideFormedDao(appDatabase: FormedDatabase): FormedDAO {
        return appDatabase.formedDao()
    }

    @Provides
    @Singleton
    fun provideFormedDatabase(@ApplicationContext appContext: Context): FormedDatabase {
        return FormedDatabase.getDatabase(appContext)
    }
}