package hu.ait.formed.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.shoppinglist.ui.data.ShoppingListDAO
import hu.ait.shoppinglist.ui.data.ShoppingListDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideShoppingListDao(appDatabase: ShoppingListDatabase): ShoppingListDAO {
        return appDatabase.shoppingDao()
    }

    @Provides
    @Singleton
    fun provideShoppingListDatabase(@ApplicationContext appContext: Context): ShoppingListDatabase {
        return ShoppingListDatabase.getDatabase(appContext)
    }
}