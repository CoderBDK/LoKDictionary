package com.coderbdk.lokdictionary.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private const val SETTINGS_PREFERENCES_NAME = "settings_preferences"

private val Context.dataStore by preferencesDataStore(
    name = SETTINGS_PREFERENCES_NAME
)

private object SettingsKeys {
    val ENABLE_DARK_MODE = booleanPreferencesKey("enable_dark_mode")
}

class SettingsPreferences(private val context: Context) {
    val isDarkMoodEnable = context.dataStore.data.map { preferences ->
        preferences[SettingsKeys.ENABLE_DARK_MODE] ?: false
    }

    suspend fun enableDarkMode(enable: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SettingsKeys.ENABLE_DARK_MODE] = enable
        }
    }
}