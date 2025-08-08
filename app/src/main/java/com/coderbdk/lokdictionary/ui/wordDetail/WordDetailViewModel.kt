package com.coderbdk.lokdictionary.ui.wordDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.repository.WordRepository
import com.coderbdk.lokdictionary.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject


data class WordDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val word: WordWithMeaning? = null
)

sealed class WordDetailUiEvent {

}

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val wordRepository: WordRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WordDetailUiState())
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    word = wordRepository.getWordWithMeaningById(savedStateHandle.toRoute<Screen.WordDetail>().wordId)
                )
            }
        }

    }

    fun onEvent(event: WordDetailUiEvent) {

    }
}