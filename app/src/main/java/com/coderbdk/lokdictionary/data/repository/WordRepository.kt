package com.coderbdk.lokdictionary.data.repository

import androidx.paging.PagingData
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    suspend fun insertWord(word: Word)

    suspend fun updateWord(word: Word)

    suspend fun deleteWord(word: Word)

    fun searchWordsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?
    ): Flow<PagingData<Word>>
}