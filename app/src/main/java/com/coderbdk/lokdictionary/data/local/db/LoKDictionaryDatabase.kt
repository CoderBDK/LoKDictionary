package com.coderbdk.lokdictionary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coderbdk.lokdictionary.data.local.db.dao.MeaningDao
import com.coderbdk.lokdictionary.data.local.db.dao.WordDao
import com.coderbdk.lokdictionary.data.local.db.entity.Meaning
import com.coderbdk.lokdictionary.data.local.db.entity.Word

@Database(
    entities = [Word::class, Meaning::class],
    version = 1
)
@TypeConverters(
    Converters::class
)
abstract class LoKDictionaryDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun meaningDao(): MeaningDao
}