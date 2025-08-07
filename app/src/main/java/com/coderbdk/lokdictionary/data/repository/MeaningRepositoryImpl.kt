package com.coderbdk.lokdictionary.data.repository

import com.coderbdk.lokdictionary.data.local.db.dao.MeaningDao
import com.coderbdk.lokdictionary.data.local.db.entity.Meaning

class MeaningRepositoryImpl(
    private val meaningDao: MeaningDao,
) : MeaningRepository {

    override suspend fun insertMeaning(meaning: Meaning) {
        meaningDao.insertMeaning(meaning)
    }

    override suspend fun updateMeaning(meaning: Meaning) {
        meaningDao.updateMeaning(meaning)
    }

    override suspend fun deleteMeaning(meaning: Meaning) {
        meaningDao.deleteMeaning(meaning)
    }

}