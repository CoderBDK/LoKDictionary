package com.coderbdk.lokdictionary.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun HomeRoute() {
    if (LocalView.current.isInEditMode) {
        HomePreview()
    } else {
        val viewModel = hiltViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val pagedWords = viewModel.pagedWords.collectAsLazyPagingItems()
        HomeScreen(
            uiState = uiState,
            pagedWords = pagedWords,
            onEvent = viewModel::onEvent
        )
    }

}