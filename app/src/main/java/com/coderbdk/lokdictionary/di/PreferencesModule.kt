package com.coderbdk.lokdictionary.di

import android.content.Context
import androidx.room.Room
import com.coderbdk.lokdictionary.data.local.db.LoKDictionaryDatabase
import com.coderbdk.lokdictionary.data.local.prefs.SettingsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {
    @Singleton
    @Provides
    fun provideSettingsPreferences(
        @ApplicationContext context: Context,
    ): SettingsPreferences {
        return SettingsPreferences(context)
    }
}