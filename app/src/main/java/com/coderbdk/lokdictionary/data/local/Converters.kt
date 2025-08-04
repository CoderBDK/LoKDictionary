package com.coderbdk.lokdictionary.data.local

import androidx.room.TypeConverter
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType

class Converters {
    @TypeConverter
    fun fromWordType(wordType: WordType): String {
        return wordType.name
    }

    @TypeConverter
    fun toWordType(value: String): WordType {
        return WordType.entries.firstOrNull { it.name == value }
            ?: WordType.UNKNOWN
    }

    @TypeConverter
    fun fromWordLanguage(wordLanguage: WordLanguage): String {
        return wordLanguage.name
    }

    @TypeConverter
    fun toWordLanguage(value: String): WordLanguage {
        return WordLanguage.entries.firstOrNull { it.name == value }
            ?: WordLanguage.UNKNOWN
    }
}