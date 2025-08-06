package com.coderbdk.lokdictionary.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkMoodEnable: Flow<Boolean>
    suspend fun enableDarkMode(enable: Boolean)
}