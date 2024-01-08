package com.jp.test.pagingwithdbdemo.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideQuoteDatabase(@ApplicationContext context: Context): QuoteDatabase {
        return Room.databaseBuilder(context, QuoteDatabase::class.java, "quoteDatabase")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideQuoteDao(quoteDatabase: QuoteDatabase): QuoteDao {
        return quoteDatabase.quoteDao()
    }

    @Provides
    @Singleton
    fun provideQuoteRemoteKeysDao(quoteDatabase: QuoteDatabase): RemoteKeysDao {
        return quoteDatabase.remoteKeysDao()
    }
}