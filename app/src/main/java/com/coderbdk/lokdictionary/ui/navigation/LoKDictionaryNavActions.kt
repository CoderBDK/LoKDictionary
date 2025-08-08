package com.coderbdk.lokdictionary.ui.navigation

import androidx.navigation.NavHostController

class LoKDictionaryNavActions(private val navController: NavHostController) {
    fun navigateToWordDetail(wordId: Long) {
        navController.navigate(Screen.WordDetail(wordId))
    }
}