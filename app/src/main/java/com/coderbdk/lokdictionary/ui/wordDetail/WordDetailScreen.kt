package com.coderbdk.lokdictionary.ui.wordDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.SwitchLeft
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.lokdictionary.data.local.db.entity.Meaning
import com.coderbdk.lokdictionary.data.local.db.entity.Word
import com.coderbdk.lokdictionary.data.local.db.entity.WordWithMeaning
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import com.coderbdk.lokdictionary.ui.theme.LoKDictionaryTheme

@Composable
fun WordDetailScreen(
    uiState: WordDetailUiState,
    onEvent: (WordDetailUiEvent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        uiState.word?.let {
            WordDetailContent(it)
        }

    }
}

@Composable
fun WordDetailContent(word: WordWithMeaning) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            Modifier
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play")
                }
                Text(text = word.word.wordPronunciation.ifEmpty { "No Pronunciation" })
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Text(
                    "\uD83D\uDCDD Note", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = word.word.note ?: "---",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 108.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                )
            }
            HorizontalDivider()
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
    }
}

@Preview(showBackground = true)
@Composable
fun WordDetailPreview() {
    LoKDictionaryTheme {
        WordDetailScreen(
            uiState = WordDetailUiState(
                word = WordWithMeaning(
                    word = Word(
                        word = "Apple",
                        wordType = WordType.NOUN,
                        wordLanguage = WordLanguage.ENGLISH,
                        wordPronunciation = "/ˈæp.əl/",
                        note = "My favorite food is Apple"
                    ),
                    meaning = Meaning(
                        wordId = 0,
                        meaning = "আপেল",
                        meaningLanguage = WordLanguage.BENGALI,
                    )
                )
            )
        ) {

        }
    }
}