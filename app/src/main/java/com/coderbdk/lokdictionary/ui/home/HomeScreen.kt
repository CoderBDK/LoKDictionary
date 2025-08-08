package com.coderbdk.lokdictionary.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SwitchLeft
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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
import com.coderbdk.lokdictionary.ui.components.SearchTextField
import com.coderbdk.lokdictionary.ui.theme.LoKDictionaryTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    pagedWords: LazyPagingItems<WordWithMeaning>,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToWordDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.showAddWordDialog) {
        AddWordDialog(
            uiState = uiState,
            onDismiss = {
                onEvent(HomeUiEvent.ShowAddWordDialog(false))
            },
            onSave = { newWord ->
                onEvent(HomeUiEvent.AddNewWord(newWord))
            }
        )
    }
    if (uiState.showEditWordDialog && uiState.editWord != null) {
        EditWordDialog(
            editWord = uiState.editWord,
            onDismiss = {
                onEvent(HomeUiEvent.ShowEditWordDialog(false, null))
            },
            onSave = { newWord ->
                onEvent(HomeUiEvent.EditWord(newWord))
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

    if (uiState.showAddOrUpdateWordNoteDialog && uiState.editWord != null) {
        AddOrUpdateWordNoteDialog(
            word = uiState.editWord.word,
            onDismiss = {
                onEvent(HomeUiEvent.ShowAddOrUpdateWordNoteDialog(false, null))
            },
            onSave = { newWord ->
                onEvent(HomeUiEvent.UpdateWordNote(newWord))
            }
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
        SearchTextField(
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
            }
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
                            WordItem(
                                uiState = uiState, index = index, word = word,
                                onEvent = onEvent,
                                onNavigateToWordDetail = onNavigateToWordDetail
                            )
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
fun AddOrUpdateWordNoteDialog(word: Word, onDismiss: () -> Unit, onSave: (Word) -> Unit) {
    var note by remember { mutableStateOf(word.note ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (note.isNotBlank()) {
                        onSave(
                            word.copy(
                                note = note.trim(),
                            )
                        )
                    }
                }
            ) {
                Text(if (word.note == null) "Save" else "Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(if (word.note == null) "Add Note" else "Update Note") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note") },
                    maxLines = 9
                )
            }
        }
    )
}

@Composable
fun AddWordDialog(
    uiState: HomeUiState,
    onDismiss: () -> Unit,
    onSave: (WordWithMeaning) -> Unit
) {
    var word by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var pronunciation by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<WordType?>(null) }
    var selectedLanguage by remember { mutableStateOf(uiState.selectedWordLanguageSettings) }
    var selectedMeaningLanguage by remember { mutableStateOf(uiState.selectedMeaningLanguageSettings) }
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
fun EditWordDialog(
    editWord: WordWithMeaning,
    onDismiss: () -> Unit,
    onSave: (WordWithMeaning) -> Unit
) {
    var word by remember { mutableStateOf(editWord.word.word) }
    var meaning by remember { mutableStateOf(editWord.meaning.meaning) }
    var pronunciation by remember { mutableStateOf(editWord.word.wordPronunciation) }
    var selectedType by remember { mutableStateOf(editWord.word.wordType) }
    var selectedLanguage by remember { mutableStateOf(editWord.word.wordLanguage) }
    var selectedMeaningLanguage by remember { mutableStateOf(editWord.meaning.meaningLanguage) }
    val wordTypes = remember { WordType.entries.drop(1) }
    val wordLanguages = remember { WordLanguage.entries.drop(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (word.isNotBlank() && pronunciation.isNotBlank()) {
                        onSave(
                            WordWithMeaning(
                                word = editWord.word.copy(
                                    word = word.trim(),
                                    wordType = selectedType,
                                    wordLanguage = selectedLanguage,
                                    wordPronunciation = pronunciation.trim()
                                ),
                                meaning = editWord.meaning.copy(
                                    meaning = meaning,
                                    meaningLanguage = selectedMeaningLanguage
                                )
                            )

                        )
                    }
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Word") },
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
                    value = selectedType.typeName,
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
                    value = selectedLanguage.languageName,
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
                    value = selectedMeaningLanguage.languageName,
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
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onToggleBookmark: () -> Unit,
    onDelete: () -> Unit,
    onWordNoteAddOrUpdate: () -> Unit,
    onNavigateToWordDetail: (Long) -> Unit
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
                onEdit()
            }
        )


        HorizontalDivider()

        DropdownMenuItem(
            text = { Text(if (word.isBookmark) "Unbookmark" else "Bookmark") },
            leadingIcon = {
                Icon(
                    if (word.isBookmark) Icons.Outlined.BookmarkRemove else Icons.Outlined.BookmarkAdd,
                    contentDescription = null
                )
            },
            onClick = {
                onDismissRequest()
                onToggleBookmark()
            }
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
            onClick = {
                onDismissRequest()
                onDelete()
            }
        )


        HorizontalDivider()
        DropdownMenuItem(
            text = { Text(if (word.note == null) "Add Note" else "Update Note") },
            leadingIcon = {
                Icon(
                    if (word.note == null) Icons.AutoMirrored.Outlined.NoteAdd else Icons.Outlined.NoteAlt,
                    contentDescription = null
                )
            },
            onClick = {
                onDismissRequest()
                onWordNoteAddOrUpdate()
            }
        )
        HorizontalDivider()


        DropdownMenuItem(
            text = { Text("Details") },
            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            onClick = {
                onDismissRequest()
                onNavigateToWordDetail(word.wordId)
            }
        )
    }
}

@Composable
fun WordItem(
    uiState: HomeUiState,
    index: Int,
    word: WordWithMeaning,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToWordDetail: (Long) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onNavigateToWordDetail(word.word.wordId)
        }
    ) {
        Column(
            Modifier
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play")
                }
                Text(text = word.word.wordPronunciation.ifEmpty { "No Pronunciation" })
                Box {
                    IconButton(onClick = {
                        onEvent(
                            HomeUiEvent.ShowDropdownMoreMenu(
                                true,
                                index
                            )
                        )
                    }) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "More Options")
                    }
                    if (uiState.showDropdownMoreMenu && uiState.showDropdownMoreMenuAtIndex == index) {
                        DropdownMenuWithMoreOptions(
                            expanded = true,
                            word = word.word,
                            onDismissRequest = {
                                onEvent(
                                    HomeUiEvent.ShowDropdownMoreMenu(
                                        false,
                                        -1
                                    )
                                )
                            },
                            onEdit = { onEvent(HomeUiEvent.ShowEditWordDialog(true, word)) },
                            onToggleBookmark = {
                                onEvent(HomeUiEvent.WordBookmark(word.word))
                            },
                            onDelete = {
                                onEvent(HomeUiEvent.DeleteWord(word.word))
                            },
                            onWordNoteAddOrUpdate = {
                                onEvent(HomeUiEvent.ShowAddOrUpdateWordNoteDialog(true, word))
                            },
                            onNavigateToWordDetail = onNavigateToWordDetail
                        )
                    }
                }
            }
            HorizontalDivider()
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("▪ ${word.word.word}")
                        append("\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                        )
                    ) {
                        append("▪ ${word.meaning.meaning}")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    enabled = false,
                    onClick = {},
                    label = {
                        Text(word.word.wordType.typeName)
                    }
                )
                Spacer(Modifier.weight(1f))
                AssistChip(
                    onClick = {},
                    label = {
                        Text(word.word.wordLanguage.languageName)
                    }
                )
                Icon(Icons.Default.SwitchLeft, "arrow", Modifier.padding(horizontal = 8.dp))
                AssistChip(
                    onClick = {},
                    label = {
                        Text(word.meaning.meaningLanguage.languageName)
                    }
                )
            }

        }
        if (false)
            ListItem(
                overlineContent = {
                    Text(text = word.word.wordPronunciation.ifEmpty { "No Pronunciation" })
                },
                headlineContent = {
                    Text(
                        text = "${word.word.word} (${word.word.wordLanguage.code})",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                supportingContent = {
                    Text(
                        text = "${word.meaning.meaning} (${word.meaning.meaningLanguage.code})",
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
                        IconButton(onClick = {
                            onEvent(
                                HomeUiEvent.ShowDropdownMoreMenu(
                                    true,
                                    index
                                )
                            )
                        }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                        }
                        if (uiState.showDropdownMoreMenu && uiState.showDropdownMoreMenuAtIndex == index) {
                            DropdownMenuWithMoreOptions(
                                expanded = true,
                                word = word.word,
                                onDismissRequest = {
                                    onEvent(
                                        HomeUiEvent.ShowDropdownMoreMenu(
                                            false,
                                            -1
                                        )
                                    )
                                },
                                onEdit = { onEvent(HomeUiEvent.ShowEditWordDialog(true, word)) },
                                onToggleBookmark = {
                                    onEvent(HomeUiEvent.WordBookmark(word.word))
                                },
                                onDelete = {
                                    onEvent(HomeUiEvent.DeleteWord(word.word))
                                },
                                onWordNoteAddOrUpdate = {
                                    onEvent(HomeUiEvent.ShowAddOrUpdateWordNoteDialog(true, word))
                                },
                                onNavigateToWordDetail = onNavigateToWordDetail
                            )
                        }
                    }
                }
            )
    }
}

@Preview(showBackground = true)
@Composable
fun WordItemPreview() {
    LoKDictionaryTheme {
        Surface {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                WordItem(
                    uiState = HomeUiState(),
                    index = 0,
                    word = WordWithMeaning(
                        word = Word(
                            word = "Apple",
                            wordType = WordType.NOUN,
                            wordLanguage = WordLanguage.ENGLISH,
                            wordPronunciation = "/ˈæp.əl/"
                        ),
                        meaning = Meaning(
                            wordId = 0,
                            meaning = "আপেল",
                            meaningLanguage = WordLanguage.BENGALI,
                        )
                    ),
                    onEvent = {},
                    onNavigateToWordDetail = {},
                )
            }
        }
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
                meaning = "চমৎকার",
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
        Surface {
            HomeScreen(
                uiState = HomeUiState(),
                pagedWords = mockPagedWords,
                onEvent = {},
                onNavigateToWordDetail = {}
            )
        }

    }
}