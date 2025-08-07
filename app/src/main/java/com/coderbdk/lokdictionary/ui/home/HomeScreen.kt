package com.coderbdk.lokdictionary.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.coderbdk.lokdictionary.R
import com.coderbdk.lokdictionary.data.local.db.entity.Meaning
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import com.coderbdk.lokdictionary.ui.components.LoKDropdownMenu
import com.coderbdk.lokdictionary.ui.theme.LoKDictionaryTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    pagedWords: LazyPagingItems<WordWithMeaning>,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.showAddWordDialog) {
        AddWordDialog(
            onDismiss = {
                onEvent(HomeUiEvent.ShowAddWordDialog(false))
            },
            onSave = { newWord ->
                onEvent(HomeUiEvent.AddNewWord(newWord))
            }
        )
    }
    if (uiState.showWordFilterDialog) {
        WordFilterDialog(
            selectedWordType = uiState.selectedWordTypeFilter,
            selectedWordLanguage = uiState.selectedWordLanguageFilter,
            selectedMeaningLanguage = uiState.selectedMeaningLanguageFilter,
            onDismiss = {
                onEvent(HomeUiEvent.ShowWordFilterDialog(false))
            },
            onApply = { type, wordLang, meaningLang ->
                onEvent(HomeUiEvent.WordFilterApply(type, wordLang, meaningLang))
            },
        )
    }

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
            Icon(
                Icons.Outlined.Book,
                "dictionary",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            )
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleMedium)
            IconButton(
                onClick = {
                    onEvent(HomeUiEvent.ShowAddWordDialog(true))
                }
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    "add_word",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
        Spacer(Modifier.height(16.dp))
        TextField(
            value = uiState.searchQuery,
            onValueChange = {
                onEvent(HomeUiEvent.SearchQueryChanged(it))
            },
            placeholder = {
                Text(stringResource(R.string.home_screen_search_text_field_hint))
            },
            leadingIcon = {
                IconButton(
                    onClick = {
                        onEvent(HomeUiEvent.ShowWordFilterDialog(true))
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
                            onEvent(HomeUiEvent.SearchQueryChanged(""))
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
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        count = pagedWords.itemCount,
                        key = pagedWords.itemKey { it.word.wordId }
                    ) { index ->
                        val word = pagedWords[index]
                        word?.let {
                            WordItem(uiState, index, word.word, word.meaning, showMoreMenu = {
                                onEvent(
                                    HomeUiEvent.ShowDropdownMoreMenu(
                                        true,
                                        index,
                                        word.word
                                    )
                                )
                            }, onDismissRequest = {
                                onEvent(
                                    HomeUiEvent.ShowDropdownMoreMenu(
                                        false,
                                        0,
                                        null
                                    )
                                )
                            })
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
fun AddWordDialog(
    onDismiss: () -> Unit,
    onSave: (WordWithMeaning) -> Unit
) {
    var word by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var pronunciation by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<WordType?>(null) }
    var selectedLanguage by remember { mutableStateOf<WordLanguage?>(null) }
    var selectedMeaningLanguage by remember { mutableStateOf<WordLanguage?>(null) }
    val wordTypes = remember { WordType.entries.drop(1) }
    val wordLanguages = remember { WordLanguage.entries.drop(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (word.isNotBlank() && pronunciation.isNotBlank() && selectedType != null && selectedLanguage != null) {
                        onSave(
                            WordWithMeaning(
                                word = Word(
                                    word = word.trim(),
                                    wordType = selectedType!!,
                                    wordLanguage = selectedLanguage!!,
                                    wordPronunciation = pronunciation.trim()
                                ),
                                meaning = Meaning(
                                    meaning = meaning,
                                    wordId = 0,
                                    meaningLanguage = selectedMeaningLanguage!!
                                )
                            )

                        )
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add New Word") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = word,
                    onValueChange = { word = it },
                    label = { Text("Word") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = { Text("Meaning") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = pronunciation,
                    onValueChange = { pronunciation = it },
                    label = { Text("Pronunciation") },
                    singleLine = true
                )

                LoKDropdownMenu(
                    value = selectedType?.typeName ?: "",
                    items = wordTypes,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_word_type))
                    },
                    itemContent = {
                        Text(it.typeName)
                    },
                    onItemSelected = {
                        selectedType = it
                    }
                )
                LoKDropdownMenu(
                    value = selectedLanguage?.languageName ?: "",
                    items = wordLanguages,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_word_language))
                    },
                    itemContent = {
                        Text(it.languageName)
                    },
                    onItemSelected = {
                        selectedLanguage = it
                    }
                )
                LoKDropdownMenu(
                    value = selectedMeaningLanguage?.languageName ?: "",
                    items = wordLanguages,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_meaning_language))
                    },
                    itemContent = {
                        Text(it.languageName)
                    },
                    onItemSelected = {
                        selectedMeaningLanguage = it
                    }
                )
            }
        }
    )
}

@Composable
fun WordFilterDialog(
    selectedWordType: WordType?,
    selectedWordLanguage: WordLanguage?,
    selectedMeaningLanguage: WordLanguage?,
    onDismiss: () -> Unit,
    onApply: (WordType?, WordLanguage?, WordLanguage?) -> Unit,
) {
    var selectedType by remember { mutableStateOf(selectedWordType) }
    var selectedLanguage by remember { mutableStateOf(selectedWordLanguage) }
    var selectedMeaningLanguage by remember { mutableStateOf(selectedMeaningLanguage) }
    val wordTypes = remember { WordType.entries.drop(1) }
    val wordLanguages = remember { WordLanguage.entries.drop(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onApply(selectedType, selectedLanguage, selectedMeaningLanguage)
            }) {
                Text(stringResource(R.string.home_screen_filter_apply))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.home_screen_filter_cancel))
            }
        },
        title = {
            Text(stringResource(R.string.home_screen_filter_words))
        },
        text = {
            Column {
                LoKDropdownMenu(
                    value = selectedType?.typeName ?: "",
                    items = wordTypes,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_word_type))
                    },
                    itemContent = {
                        Text(it.typeName)
                    },
                    onItemSelected = {
                        selectedType = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LoKDropdownMenu(
                    value = selectedLanguage?.languageName ?: "",
                    items = wordLanguages,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_word_language))
                    },
                    itemContent = {
                        Text(it.languageName)
                    },
                    onItemSelected = {
                        selectedLanguage = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LoKDropdownMenu(
                    value = selectedMeaningLanguage?.languageName ?: "",
                    items = wordLanguages,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_meaning_language))
                    },
                    itemContent = {
                        Text(it.languageName)
                    },
                    onItemSelected = {
                        selectedMeaningLanguage = it
                    }
                )
                TextButton(
                    onClick = {
                        selectedType = null
                        selectedLanguage = null
                        selectedMeaningLanguage = null
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.home_screen_filter_clear_filters))
                }
            }
        }
    )
}

@Composable
fun DropdownMenuWithMoreOptions(
    expanded: Boolean,
    word: Word,
    onDismissRequest: () -> Unit
) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {

        DropdownMenuItem(
            text = { Text("Edit") },
            leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
            onClick = {
                onDismissRequest()
            }
        )


        HorizontalDivider()

        DropdownMenuItem(
            text = { Text("Bookmark") },
            leadingIcon = { Icon(Icons.Outlined.BookmarkAdd, contentDescription = null) },
            onClick = {
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Add Note") },
            leadingIcon = { Icon(Icons.AutoMirrored.Outlined.NoteAdd, contentDescription = null) },
            onClick = {
                onDismissRequest()
            }
        )
        HorizontalDivider()


        DropdownMenuItem(
            text = { Text("Details") },
            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            onClick = {
                onDismissRequest()
            }
        )
    }
}

@Composable
fun WordItem(
    uiState: HomeUiState,
    index: Int,
    word: Word,
    meaning: Meaning,
    showMoreMenu: (Word) -> Unit,
    onDismissRequest: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        ListItem(
            overlineContent = {
                Text(text = word.wordPronunciation.ifEmpty { "No Pronunciation" })
            },
            headlineContent = {
                Text(
                    text = "${word.word} (${word.wordLanguage.code})",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    text = "${meaning.meaning} (${meaning.meaningLanguage.code})",
                    style = MaterialTheme.typography.bodySmall
                )
            },
            leadingContent = {
                IconButton(onClick = { }) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play")
                }
            },
            trailingContent = {
                Box {
                    IconButton(onClick = { showMoreMenu(word) }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }

                    if (uiState.showDropdownMoreMenu && uiState.showDropdownMoreMenuAtIndex == index) {
                        DropdownMenuWithMoreOptions(
                            expanded = true,
                            word = word,
                            onDismissRequest = onDismissRequest
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val sampleWords = listOf(
        WordWithMeaning(
            word = Word(
                word = "Apple",
                wordType = WordType.NOUN,
                wordLanguage = WordLanguage.ENGLISH,
                wordPronunciation = "/ˈæp.əl/"
            ),
            meaning = Meaning(
                wordId = 0,
                meaning = "",
                meaningLanguage = WordLanguage.BENGALI,
            )
        ),
        WordWithMeaning(
            word = Word(
                word = "Run",
                wordType = WordType.VERB,
                wordLanguage = WordLanguage.ENGLISH,
                wordPronunciation = "/rʌn/"
            ),
            meaning = Meaning(
                wordId = 0,
                meaning = "",
                meaningLanguage = WordLanguage.BENGALI,
            )
        ),
        WordWithMeaning(
            word = Word(
                word = "Beautiful",
                wordType = WordType.ADJECTIVE,
                wordLanguage = WordLanguage.ENGLISH,
                wordPronunciation = "/ˈbjuː.tɪ.fəl/"
            ),
            meaning = Meaning(
                wordId = 0,
                meaning = "",
                meaningLanguage = WordLanguage.BENGALI,
            )
        ),
        WordWithMeaning(
            word = Word(
                word = "Quickly",
                wordType = WordType.ADVERB,
                wordLanguage = WordLanguage.ENGLISH,
                wordPronunciation = "/ˈkwɪk.li/"
            ),
            meaning = Meaning(
                wordId = 0,
                meaning = "",
                meaningLanguage = WordLanguage.BENGALI,
            )
        ),
        WordWithMeaning(
            word = Word(
                word = "Joy",
                wordType = WordType.NOUN,
                wordLanguage = WordLanguage.ENGLISH,
                wordPronunciation = "/dʒɔɪ/"
            ),
            meaning = Meaning(
                wordId = 0,
                meaning = "",
                meaningLanguage = WordLanguage.BENGALI,
            )
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