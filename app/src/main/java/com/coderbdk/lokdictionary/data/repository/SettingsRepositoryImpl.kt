package com.coderbdk.lokdictionary.data.repository

import com.coderbdk.lokdictionary.data.local.prefs.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val settingsPrefs: SettingsPreferences) :
    SettingsRepository {
    override val isDarkMoodEnable: Flow<Boolean> = settingsPrefs.isDarkMoodEnable

    override suspend fun enableDarkMode(enable: Boolean) {
        settingsPrefs.enableDarkMode(enable)
    }
}