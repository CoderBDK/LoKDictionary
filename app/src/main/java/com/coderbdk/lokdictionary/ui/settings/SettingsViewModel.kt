package com.coderbdk.lokdictionary.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val isDarkModeEnable: Boolean = false
)

sealed class SettingsUiEvent {
    data class EnableDarkMode(val enable: Boolean) : SettingsUiEvent()
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
                    isDarkModeEnable = settingsRepository.isDarkMoodEnable.first()
                )
            }
        }
    }


    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.EnableDarkMode -> enableDarkMode(event.enable)
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

}