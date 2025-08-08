package com.coderbdk.lokdictionary.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Serializable
data class WordWithMeaning(
    @Embedded val word: Word,
    @Relation(
        parentColumn = "word_id",
        entityColumn = "word_id"
    )
    val meaning: Meaning
)
