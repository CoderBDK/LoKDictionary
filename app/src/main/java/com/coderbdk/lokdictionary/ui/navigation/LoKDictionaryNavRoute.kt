package com.coderbdk.lokdictionary.ui.navigation

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