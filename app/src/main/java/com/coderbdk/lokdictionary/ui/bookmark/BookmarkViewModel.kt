package com.coderbdk.lokdictionary.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import com.coderbdk.lokdictionary.data.repository.SettingsRepository
import com.coderbdk.lokdictionary.data.repository.WordRepository
import com.coderbdk.lokdictionary.ui.home.HomeUiEvent
import com.coderbdk.lokdictionary.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarkUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedWordTypeFilter: WordType? = null,
    val selectedWordLanguageFilter: WordLanguage? = null,
    val selectedMeaningLanguageFilter: WordLanguage? = null,
    val showWordFilterDialog: Boolean = false,
    val errorMessage: String? = null,
)

sealed class BookmarkUiEvent {
    data class SearchQueryChanged(val query: String) : BookmarkUiEvent()
    data class WordFilterApply(
        val wordType: WordType?,
        val wordLanguage: WordLanguage?,
        val meaningLanguage: WordLanguage?
    ) :
        BookmarkUiEvent()

    object ClearSearchFilters : BookmarkUiEvent()
    data class ShowWordFilterDialog(val mode: Boolean) : BookmarkUiEvent()
    data class BookmarkRemove(val word: Word) : BookmarkUiEvent()
}

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagedWords: Flow<PagingData<WordWithMeaning>> = combine(
        _uiState.map { it.searchQuery }.debounce(350L).distinctUntilChanged(),
        _uiState.map { it.selectedWordTypeFilter }.distinctUntilChanged(),
        _uiState.map { it.selectedWordLanguageFilter }.distinctUntilChanged(),
        _uiState.map { it.selectedMeaningLanguageFilter }.distinctUntilChanged(),
    ) { query, wordType, wordLanguage, meaningLanguage ->
        wordRepository.searchBookmarksWordsWithMeaningsPagingSource(
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

    fun onEvent(event: BookmarkUiEvent) {
        when (event) {
            is BookmarkUiEvent.SearchQueryChanged -> handleSearchQueryChanged(event.query)
            is BookmarkUiEvent.WordFilterApply -> applyWordFilter(
                event.wordType,
                event.wordLanguage,
                event.meaningLanguage
            )

            is BookmarkUiEvent.ClearSearchFilters -> clearSearchFilters()
            is BookmarkUiEvent.ShowWordFilterDialog -> showWordFilterDialog(event.mode)
            is BookmarkUiEvent.BookmarkRemove -> bookmarkRemoved(event.word)
        }
    }

    private fun bookmarkRemoved(word: Word) {
        viewModelScope.launch {
            wordRepository.updateWord(
                word.copy(
                    isBookmark = false
                )
            )
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
}