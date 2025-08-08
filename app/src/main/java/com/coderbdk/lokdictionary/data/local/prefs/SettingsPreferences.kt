package com.coderbdk.lokdictionary.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.coderbdk.lokdictionary.data.model.WordLanguage
import kotlinx.coroutines.flow.map

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"

private val Context.dataStore by preferencesDataStore(
    name = SETTINGS_PREFERENCES_NAME
)

private object SettingsKeys {
    val ENABLE_DARK_MODE = booleanPreferencesKey("enable_dark_mode")
    val SELECTED_WORD_LANGUAGE = stringPreferencesKey("selected_word_language")
    val SELECTED_MEANING_LANGUAGE = stringPreferencesKey("selected_meaning_language")
}

class SettingsPreferences(private val context: Context) {
    val isDarkModeEnable = context.dataStore.data.map { preferences ->
        preferences[SettingsKeys.ENABLE_DARK_MODE] ?: false
    }
    val selectedWordLanguage = context.dataStore.data.map { preferences ->
        WordLanguage.valueOf(
            preferences[SettingsKeys.SELECTED_WORD_LANGUAGE] ?: WordLanguage.ENGLISH.name
        )
    }
    val selectedMeaningLanguage = context.dataStore.data.map { preferences ->
        WordLanguage.valueOf(
            preferences[SettingsKeys.SELECTED_MEANING_LANGUAGE] ?: WordLanguage.BENGALI.name
        )
    }

    suspend fun enableDarkMode(enable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SettingsKeys.ENABLE_DARK_MODE] = enable
        }
    }

    suspend fun updateWordLanguage(language: WordLanguage) {
        context.dataStore.edit { preferences ->
            preferences[SettingsKeys.SELECTED_WORD_LANGUAGE] = language.name
        }
    }

    suspend fun updateMeaningLanguage(language: WordLanguage) {
        context.dataStore.edit { preferences ->
            preferences[SettingsKeys.SELECTED_MEANING_LANGUAGE] = language.name
        }
    }
}