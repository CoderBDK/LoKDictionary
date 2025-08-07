package com.coderbdk.lokdictionary.ui.bookmark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun BookmarkRoute() {
    val viewModel = hiltViewModel<BookmarkViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagedWords = viewModel.pagedWords.collectAsLazyPagingItems()
    BookScreen(
        uiState = uiState,
        pagedWords = pagedWords,
        onEvent = viewModel::onEvent
    )
}