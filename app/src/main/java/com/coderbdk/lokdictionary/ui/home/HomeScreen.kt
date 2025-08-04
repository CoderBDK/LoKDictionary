package com.coderbdk.lokdictionary.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.coderbdk.lokdictionary.R
import com.coderbdk.lokdictionary.data.local.entity.Word
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import com.coderbdk.lokdictionary.ui.theme.LoKDictionaryTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    pagedWords: LazyPagingItems<Word>,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Book, "dictionary")
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleMedium)
            IconButton(
                onClick = {
                    onEvent(
                        HomeUiEvent.AddNewWord(
                            Word(
                                word = Array((3..8).random()) {
                                    (65..86).random().toChar()
                                }.joinToString(""),
                                wordType = WordType.NOUN,
                                wordLanguage = WordLanguage.ENGLISH,
                                wordPronunciation = ""
                            )
                        )
                    )
                }
            ) {
                Icon(Icons.Default.AddCircle, "add_word")
            }

        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = {
                onEvent(HomeUiEvent.SearchQueryChanged(it))
            },
            placeholder = {
                Text(stringResource(R.string.home_screen_search_text_field_hint))
            },
            leadingIcon = {
                Icon(
                    Icons.Default.FilterList, "filter",
                    Modifier.clickable {
                        onEvent(HomeUiEvent.SearchQueryChanged(""))
                    })
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    Icon(
                        Icons.Default.Clear, "clear",
                        Modifier.clickable {
                            onEvent(HomeUiEvent.SearchQueryChanged(""))
                        })
                }
            },
            shape = CircleShape,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = pagedWords.itemCount,
                key = pagedWords.itemKey { it.id }
            ) { index ->
                val word = pagedWords[index]
                word?.let {
                    WordItem(word)
                }
            }
        }
    }

}

@Composable
fun WordItem(word: Word) {
    ListItem(
        overlineContent = {
            Text(text = word.wordPronunciation.ifEmpty { "No Pronunciation" })
        },
        headlineContent = {
            Text(text = word.word)
        },
        supportingContent = {
            Text(text = "${word.wordType.typeName} (${word.wordLanguage.languageName})")
        },
        leadingContent = {
            IconButton(
                onClick = {}
            ) {
                Icon(Icons.AutoMirrored.Filled.VolumeUp, "clear")
            }

        },
        trailingContent = {
            IconButton(onClick = {

            }) {
                Icon(Icons.Default.MoreVert, "more")
            }

        },
        modifier = Modifier.fillMaxWidth()
    )
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val sampleWords = listOf(
        Word(
            word = "Apple",
            wordType = WordType.NOUN,
            wordLanguage = WordLanguage.ENGLISH,
            wordPronunciation = "/ˈæp.əl/"
        )
    )
    val mockPagedWords = flowOf(PagingData.from(sampleWords)).collectAsLazyPagingItems()
    LoKDictionaryTheme {
        HomeScreen(
            uiState = HomeUiState(),
            pagedWords = mockPagedWords,
            onEvent = {}
        )
    }
}