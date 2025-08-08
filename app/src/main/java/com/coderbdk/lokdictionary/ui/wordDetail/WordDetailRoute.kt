package com.coderbdk.lokdictionary.ui.wordDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WordDetailRoute() {
    val viewModel = hiltViewModel<WordDetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WordDetailScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}