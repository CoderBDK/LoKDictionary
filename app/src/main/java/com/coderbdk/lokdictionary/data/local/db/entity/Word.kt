package com.coderbdk.lokdictionary.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("word_id")
    val wordId: Long = 0,
    @ColumnInfo(name = "word")
    val word: String,
    @ColumnInfo(name = "word_type")
    val wordType: WordType,
    @ColumnInfo(name = "word_language")
    val wordLanguage: WordLanguage,
    @ColumnInfo(name = "word_pronunciation")
    val wordPronunciation: String,
    @ColumnInfo(name = "is_bookmark")
    val isBookmark: Boolean = false
)