package com.coderbdk.lokdictionary.ui.bookmark

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.coderbdk.lokdictionary.R
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.ui.home.WordFilterDialog

@Composable
fun BookScreen(
    uiState: BookmarkUiState,
    pagedWords: LazyPagingItems<WordWithMeaning>,
    onEvent: (BookmarkUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.showWordFilterDialog) {
        WordFilterDialog(
            selectedWordType = uiState.selectedWordTypeFilter,
            selectedWordLanguage = uiState.selectedWordLanguageFilter,
            selectedMeaningLanguage = uiState.selectedMeaningLanguageFilter,
            onDismiss = {
                onEvent(BookmarkUiEvent.ShowWordFilterDialog(false))
            },
            onApply = { type, wordLang, meaningLang ->
                onEvent(BookmarkUiEvent.WordFilterApply(type, wordLang, meaningLang))
            },
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = uiState.searchQuery,
            onValueChange = {
                onEvent(BookmarkUiEvent.SearchQueryChanged(it))
            },
            placeholder = {
                Text(stringResource(R.string.home_screen_search_text_field_hint))
            },
            leadingIcon = {
                IconButton(
                    onClick = {
                        onEvent(BookmarkUiEvent.ShowWordFilterDialog(true))
                    }
                ) {
                    Icon(Icons.Default.FilterList, "filter")
                }
            },
            singleLine = true,
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onEvent(BookmarkUiEvent.SearchQueryChanged(""))
                        }
                    ) {
                        Icon(
                            Icons.Default.Clear, "clear",
                        )
                    }

                }
            },
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 1.dp,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape
                )
        )


        Spacer(Modifier.height(16.dp))

        when {
            pagedWords.loadState.refresh is LoadState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            pagedWords.itemCount == 0 -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.home_screen_no_items_found),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                LazyColumn (
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = pagedWords.itemCount,
                        key = pagedWords.itemKey { it.word.wordId }
                    ) { index ->
                        val word = pagedWords[index]
                        word?.let {
                            BookmarkItem(word, onEvent)
                        }
                    }

                    if (pagedWords.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    if (pagedWords.loadState.append is LoadState.Error) {
                        item {
                            Text(
                                text = stringResource(R.string.home_screen_error_loading),
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun BookmarkItem(word: WordWithMeaning, onEvent: (BookmarkUiEvent) -> Unit) {
    OutlinedCard(
        Modifier.padding(8.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(word.word.word)
            },
            supportingContent = {
                Text(word.meaning.meaning)
            },
            trailingContent = {
                IconButton(
                    onClick = {
                        onEvent(BookmarkUiEvent.BookmarkRemove(word.word))
                    }
                ) {
                    Icon(
                        Icons.Outlined.BookmarkRemove,
                        contentDescription = null
                    )
                }
            }
        )
    }

}