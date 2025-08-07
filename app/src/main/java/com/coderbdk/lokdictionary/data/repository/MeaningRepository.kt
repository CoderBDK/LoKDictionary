package com.coderbdk.lokdictionary.data.repository

import com.coderbdk.lokdictionary.data.local.db.entity.Meaning

interface MeaningRepository {
    suspend fun insertMeaning(meaning: Meaning)
    suspend fun updateMeaning(meaning: Meaning)
    suspend fun deleteMeaning(meaning: Meaning)
}