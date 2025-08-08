package com.coderbdk.lokdictionary.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.repository.SettingsRepository
import com.coderbdk.lokdictionary.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isDarkModeEnable: Boolean = false,
    val selectedWordLanguage: WordLanguage = WordLanguage.ENGLISH,
    val selectedMeaningLanguage: WordLanguage = WordLanguage.BENGALI,
)

sealed class SettingsUiEvent {
    data class EnableDarkMode(val enable: Boolean) : SettingsUiEvent()
    data class SelectWordLanguage(val language: WordLanguage) : SettingsUiEvent()
    data class SelectMeaningLanguage(val language: WordLanguage) : SettingsUiEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDarkModeEnable = settingsRepository.isDarkModeEnable.first(),
                    selectedWordLanguage = settingsRepository.selectedWordLanguage.first(),
                    selectedMeaningLanguage = settingsRepository.selectedMeaningLanguage.first()
                )
            }
        }
    }


    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.EnableDarkMode -> enableDarkMode(event.enable)
            is SettingsUiEvent.SelectWordLanguage -> selectWordLanguage(event.language)
            is SettingsUiEvent.SelectMeaningLanguage -> selectMeaningLanguage(event.language)
        }
    }


    private fun enableDarkMode(enable: Boolean) {
        viewModelScope.launch {
            settingsRepository.enableDarkMode(enable)
            _uiState.update {
                it.copy(enable)
            }
        }

    }

    private fun selectWordLanguage(language: WordLanguage) {
        viewModelScope.launch {
            if (uiState.value.selectedWordLanguage != language) settingsRepository.updateWordLanguage(
                language
            )
            _uiState.update {
                it.copy(selectedWordLanguage = language)
            }
        }

    }

    private fun selectMeaningLanguage(language: WordLanguage) {
        viewModelScope.launch {
            if (uiState.value.selectedMeaningLanguage != language) settingsRepository.updateMeaningLanguage(
                language
            )
            _uiState.update {
                it.copy(selectedMeaningLanguage = language)
            }
        }
    }
}