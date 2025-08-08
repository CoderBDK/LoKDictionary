package com.coderbdk.lokdictionary.ui.navigation

import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Bookmark : Screen()

    @Serializable
    data object Settings : Screen()

    @Serializable
    data class WordDetail(val wordId: Long): Screen()
}