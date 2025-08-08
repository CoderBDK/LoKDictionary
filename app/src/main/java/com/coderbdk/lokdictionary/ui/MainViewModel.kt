package com.coderbdk.lokdictionary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coderbdk.lokdictionary.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isDarkTheme: Boolean
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState(false))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.isDarkModeEnable.collectLatest { enable ->
                _uiState.update {
                    it.copy(
                        isDarkTheme = enable
                    )
                }
            }

        }
    }
}