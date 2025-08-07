package com.coderbdk.lokdictionary.di

import com.coderbdk.lokdictionary.data.local.db.dao.MeaningDao
import com.coderbdk.lokdictionary.data.local.db.dao.WordDao
import com.coderbdk.lokdictionary.data.local.prefs.SettingsPreferences
import com.coderbdk.lokdictionary.data.repository.MeaningRepository
import com.coderbdk.lokdictionary.data.repository.MeaningRepositoryImpl
import com.coderbdk.lokdictionary.data.repository.SettingsRepository
import com.coderbdk.lokdictionary.data.repository.SettingsRepositoryImpl
import com.coderbdk.lokdictionary.data.repository.WordRepository
import com.coderbdk.lokdictionary.data.repository.WordRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideWordRepository(
        dao: WordDao
    ): WordRepository {
        return WordRepositoryImpl(dao)
    }

    @Singleton
    @Provides
    fun provideSettingsRepository(
        prefs: SettingsPreferences
    ): SettingsRepository {
        return SettingsRepositoryImpl(prefs)
    }

    @Singleton
    @Provides
    fun provideMeaningRepository(
        dao: MeaningDao
    ): MeaningRepository {
        return MeaningRepositoryImpl(dao)
    }
}