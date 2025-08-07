package com.coderbdk.lokdictionary.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.coderbdk.lokdictionary.data.model.WordLanguage

@Entity(
    "meanings",
    indices = [
        Index(value = ["word_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["word_id"],
            childColumns = ["word_id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class Meaning(
    @PrimaryKey(true)
    @ColumnInfo("meaning_id")
    val meaningId: Long = 0,
    @ColumnInfo("word_id")
    val wordId: Long,
    @ColumnInfo("meaning")
    val meaning: String,
    @ColumnInfo(name = "meaning_language")
    val meaningLanguage: WordLanguage,
)