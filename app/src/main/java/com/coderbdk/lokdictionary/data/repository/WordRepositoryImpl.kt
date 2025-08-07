package com.coderbdk.lokdictionary.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.coderbdk.lokdictionary.data.local.db.dao.WordDao
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(private val wordDao: WordDao) : WordRepository {
    override suspend fun insertWord(word: Word): Long {
        return wordDao.insertWord(word)
    }

    override suspend fun updateWord(word: Word) {
        wordDao.updateWord(word)
    }

    override suspend fun deleteWord(word: Word) {
        wordDao.deleteWord(word)
    }

    override fun searchWordsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?
    ): Flow<PagingData<Word>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                wordDao.searchWordsPagingSource(
                    searchQuery = searchQuery,
                    wordType = wordType,
                    wordLanguage = wordLanguage
                )
            }
        ).flow
    }

    override fun searchWordsWithMeaningsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?,
        meaningLanguage: WordLanguage?
    ): Flow<PagingData<WordWithMeaning>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                wordDao.searchWordsWithMeaningsPagingSource(
                    searchQuery = searchQuery,
                    wordType = wordType,
                    wordLanguage = wordLanguage,
                    meaningLanguage = meaningLanguage
                )
            }
        ).flow
    }
}