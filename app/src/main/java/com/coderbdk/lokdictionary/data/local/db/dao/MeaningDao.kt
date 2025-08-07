package com.coderbdk.lokdictionary.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.coderbdk.lokdictionary.data.local.db.entity.Meaning

@Dao
interface MeaningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeaning(meaning: Meaning)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMeaning(meaning: Meaning)

    @Delete
    suspend fun deleteMeaning(meaning: Meaning)
}