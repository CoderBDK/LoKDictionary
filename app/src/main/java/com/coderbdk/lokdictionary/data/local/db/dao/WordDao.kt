package com.coderbdk.lokdictionary.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)

    @Upsert
    suspend fun upsertWord(word: Word)

    @Query(
        """
        SELECT * FROM words
        WHERE (word LIKE '%' || :searchQuery || '%')
        AND (:wordType IS NULL OR word_type = :wordType)
        AND (:wordLanguage IS NULL OR word_language = :wordLanguage)
        ORDER BY word ASC
    """
    )
    fun searchWordsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?
    ): PagingSource<Int, Word>

    @Transaction
    @Query(
        """
    SELECT words.*, meanings.* FROM words
    LEFT JOIN meanings ON words.word_id = meanings.word_id
    WHERE (words.word LIKE '%' || :searchQuery || '%')
    AND (:wordType IS NULL OR words.word_type = :wordType)
    AND (:wordLanguage IS NULL OR words.word_language = :wordLanguage)
    AND (:meaningLanguage IS NULL OR meanings.meaning_language = :meaningLanguage)
    ORDER BY words.word ASC
"""
    )
    fun searchWordsWithMeaningsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?,
        meaningLanguage: WordLanguage?
    ): PagingSource<Int, WordWithMeaning>

    @Transaction
    @Query(
        """
    SELECT words.*, meanings.* FROM words
    LEFT JOIN meanings ON words.word_id = meanings.word_id
    WHERE (words.word LIKE '%' || :searchQuery || '%')
    AND (:wordType IS NULL OR words.word_type = :wordType)
    AND (:wordLanguage IS NULL OR words.word_language = :wordLanguage)
    AND (:meaningLanguage IS NULL OR meanings.meaning_language = :meaningLanguage)
    AND words.is_bookmark = 1
    ORDER BY words.word ASC
"""
    )
    fun searchBookmarksWordsWithMeaningsPagingSource(
        searchQuery: String,
        wordType: WordType?,
        wordLanguage: WordLanguage?,
        meaningLanguage: WordLanguage?
    ): PagingSource<Int, WordWithMeaning>

    @Transaction
    @Query("SELECT * from words WHERE word_id = :wordId")
    suspend fun getWordWithMeaningById(wordId: Long): WordWithMeaning?

}