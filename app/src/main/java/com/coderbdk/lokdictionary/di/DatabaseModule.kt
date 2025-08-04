package com.coderbdk.lokdictionary.di

import android.content.Context
import androidx.room.Room
import com.coderbdk.lokdictionary.data.local.LoKDictionaryDatabase
import com.coderbdk.lokdictionary.data.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): LoKDictionaryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LoKDictionaryDatabase::class.java,
            "lok_dictionary_database"
        ).build()
    }

    @Provides
    fun provideWordDao(database: LoKDictionaryDatabase): WordDao {
        return database.wordDao()
    }
}