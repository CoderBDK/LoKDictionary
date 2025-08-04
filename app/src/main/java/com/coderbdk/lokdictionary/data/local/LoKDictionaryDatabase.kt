package com.coderbdk.lokdictionary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coderbdk.lokdictionary.data.local.dao.WordDao
import com.coderbdk.lokdictionary.data.local.entity.Word

@Database(
    entities = [Word::class],
    version = 1
)
@TypeConverters(
    Converters::class
)
abstract class LoKDictionaryDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}