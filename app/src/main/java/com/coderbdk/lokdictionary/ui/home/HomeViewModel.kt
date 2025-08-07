package com.coderbdk.lokdictionary.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import com.coderbdk.lokdictionary.data.repository.MeaningRepository
import com.coderbdk.lokdictionary.data.repository.SettingsRepository
import com.coderbdk.lokdictionary.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedWordTypeFilter: WordType? = null,
    val selectedWordLanguageFilter: WordLanguage? = null,
    val selectedMeaningLanguageFilter: WordLanguage? = null,
    val showAddWordDialog: Boolean = false,
    val showEditWordDialog: Boolean = false,
    val showDropdownMoreMenu: Boolean = false,
    val showWordFilterDialog: Boolean = false,
    val showDropdownMoreMenuAtIndex: Int = 0,
    val editWord: Word? = null,
    val errorMessage: String? = null,
)

sealed class HomeUiEvent {
    data class ShowAddWordDialog(val mode: Boolean) : HomeUiEvent()
    data class AddNewWord(val word: WordWithMeaning) : HomeUiEvent()
    data class ShowEditWordDialog(val mode: Boolean) : HomeUiEvent()
    data class EditWord(val word: Word) : HomeUiEvent()
    data class DeleteWord(val word: Word) : HomeUiEvent()
    data class SearchQueryChanged(val query: String) : HomeUiEvent()
    data class WordFilterApply(
        val wordType: WordType?,
        val wordLanguage: WordLanguage?,
        val meaningLanguage: WordLanguage?
    ) :
        HomeUiEvent()

    object ClearSearchFilters : HomeUiEvent()
    data class ShowDropdownMoreMenu(val mode: Boolean, val showAtIndex: Int, val word: Word?) :
        HomeUiEvent()

    data class ShowWordFilterDialog(val mode: Boolean) : HomeUiEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val meaningRepository: MeaningRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagedWords: Flow<PagingData<WordWithMeaning>> = combine(
        _uiState.map { it.searchQuery }.debounce(350L).distinctUntilChanged(),
        _uiState.map { it.selectedWordTypeFilter }.distinctUntilChanged(),
        _uiState.map { it.selectedWordLanguageFilter }.distinctUntilChanged(),
        _uiState.map { it.selectedMeaningLanguageFilter }.distinctUntilChanged(),
    ) { query, wordType, wordLanguage, meaningLanguage ->
        wordRepository.searchWordsWithMeaningsPagingSource(
            query,
            wordType,
            wordLanguage,
            meaningLanguage
        )
    }.flatMapLatest { it }
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            combine(
                settingsRepository.selectedWordLanguage,
                settingsRepository.selectedMeaningLanguage
            ) { wordLanguage, meaningLanguage ->
                _uiState.update {
                    it.copy(
                        selectedWordLanguageFilter = wordLanguage,
                        selectedMeaningLanguageFilter = meaningLanguage
                    )
                }
            }.collect()

        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.AddNewWord -> addNewWord(event.word)
            is HomeUiEvent.DeleteWord -> deleteWord(event.word)
            is HomeUiEvent.EditWord -> editWord(event.word)
            is HomeUiEvent.SearchQueryChanged -> handleSearchQueryChanged(event.query)
            is HomeUiEvent.WordFilterApply -> applyWordFilter(
                event.wordType,
                event.wordLanguage,
                event.meaningLanguage
            )

            is HomeUiEvent.ClearSearchFilters -> clearSearchFilters()
            is HomeUiEvent.ShowAddWordDialog -> showAddWordDialog(event.mode)
            is HomeUiEvent.ShowEditWordDialog -> showEditWordDialog(event.mode)
            is HomeUiEvent.ShowDropdownMoreMenu -> showDropdownMoreMenu(
                event.mode,
                event.showAtIndex,
                event.word
            )

            is HomeUiEvent.ShowWordFilterDialog -> showWordFilterDialog(event.mode)
        }
    }

    private fun applyWordFilter(
        wordType: WordType?,
        wordLanguage: WordLanguage?,
        meaningLanguage: WordLanguage?
    ) {
        _uiState.update {
            it.copy(
                selectedWordTypeFilter = wordType,
                selectedWordLanguageFilter = wordLanguage,
                selectedMeaningLanguageFilter = meaningLanguage,
                showWordFilterDialog = false
            )
        }
    }

    private fun showWordFilterDialog(mode: Boolean) {
        _uiState.update {
            it.copy(
                showWordFilterDialog = mode,
            )
        }
    }

    private fun showDropdownMoreMenu(mode: Boolean, showAtIndex: Int, word: Word?) {
        _uiState.update {
            it.copy(
                showDropdownMoreMenu = mode,
                showDropdownMoreMenuAtIndex = showAtIndex,
                editWord = word
            )
        }
    }

    private fun addNewWord(word: WordWithMeaning) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showAddWordDialog = false,
                )
            }
            val wordId = wordRepository.insertWord(word.word)
            meaningRepository.insertMeaning(
                word.meaning.copy(
                    wordId = wordId
                )
            )
        }
    }

    private fun deleteWord(word: Word) {
        viewModelScope.launch {
            wordRepository.deleteWord(word)
        }
    }

    private fun editWord(word: Word) {
        viewModelScope.launch {
            wordRepository.updateWord(word)
        }
    }

    private fun handleSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }


    private fun clearSearchFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedWordLanguageFilter = null,
                selectedWordTypeFilter = null
            )
        }
    }

    private fun showAddWordDialog(mode: Boolean) {
        _uiState.update { it.copy(showAddWordDialog = mode) }
    }


    private fun showEditWordDialog(mode: Boolean) {
        _uiState.update {
            it.copy(
                showEditWordDialog = mode,
            )
        }
    }
}