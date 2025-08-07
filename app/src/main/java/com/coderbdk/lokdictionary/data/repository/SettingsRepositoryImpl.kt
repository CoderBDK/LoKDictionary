package com.coderbdk.lokdictionary.data.repository

import com.coderbdk.lokdictionary.data.local.prefs.SettingsPreferences
import com.coderbdk.lokdictionary.data.model.WordLanguage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val settingsPrefs: SettingsPreferences) :
    SettingsRepository {
    override val isDarkMoodEnable: Flow<Boolean> = settingsPrefs.isDarkMoodEnable
    override val selectedWordLanguage: Flow<WordLanguage> = settingsPrefs.selectedWordLanguage
    override val selectedMeaningLanguage: Flow<WordLanguage> = settingsPrefs.selectedMeaningLanguage

    override suspend fun enableDarkMode(enable: Boolean) {
        settingsPrefs.enableDarkMode(enable)
    }

    override suspend fun updateWordLanguage(language: WordLanguage) {
        settingsPrefs.updateWordLanguage(language)
    }

    override suspend fun updateMeaningLanguage(language: WordLanguage) {
        settingsPrefs.updateMeaningLanguage(language)
    }

}