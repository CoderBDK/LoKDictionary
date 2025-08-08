package com.coderbdk.lokdictionary.data.repository

import com.coderbdk.lokdictionary.data.model.WordLanguage
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkModeEnable: Flow<Boolean>
    val selectedWordLanguage: Flow<WordLanguage>
    val selectedMeaningLanguage: Flow<WordLanguage>

    suspend fun enableDarkMode(enable: Boolean)
    suspend fun updateWordLanguage(language: WordLanguage)
    suspend fun updateMeaningLanguage(language: WordLanguage)
}