package com.coderbdk.lokdictionary.di

import com.coderbdk.lokdictionary.data.local.dao.WordDao
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
}