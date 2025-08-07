package com.coderbdk.lokdictionary.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    suspend fun insertWord(word: Word): Long

    suspend fun updateWord(word: Word)

    suspend fun deleteWord(word: Word)

    fun searchWordsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?
    ): Flow<PagingData<Word>>


    fun searchWordsWithMeaningsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?,
        meaningLanguage: WordLanguage?
    ): Flow<PagingData<WordWithMeaning>>

    fun searchBookmarksWordsWithMeaningsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?,
        meaningLanguage: WordLanguage?
    ): Flow<PagingData<WordWithMeaning>>
}