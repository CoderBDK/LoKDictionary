package com.coderbdk.lokdictionary.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coderbdk.lokdictionary.R
import com.coderbdk.lokdictionary.data.model.WordLanguage
import com.coderbdk.lokdictionary.data.model.WordType
import com.coderbdk.lokdictionary.ui.components.LoKDropdownMenu
import com.coderbdk.lokdictionary.ui.theme.LoKDictionaryTheme
import com.coderbdk.lokdictionary.ui.theme.Typography

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ThemePrefs(uiState.isDarkModeEnable) { enable ->
            onEvent(SettingsUiEvent.EnableDarkMode(enable))
        }
        WordAndMeaningLanguagePrefs(
            selectedWordLanguage = uiState.selectedWordLanguage,
            selectedMeaningLanguage = uiState.selectedMeaningLanguage,
            onSelectWordLanguage = {
                onEvent(SettingsUiEvent.SelectWordLanguage(language = it))
            },
            onSelectMeaningLanguage = {
                onEvent(SettingsUiEvent.SelectMeaningLanguage(language = it))
            }
        )
    }
}

@Composable
fun ThemePrefs(
    isEnable: Boolean,
    onDarkThemeEnable: (Boolean) -> Unit,
) {
    SettingContent(
        title = "App Theme",
    ) {
        ListItem(
            leadingContent = {
                Icon(Icons.Default.BrightnessAuto, contentDescription = "Theme")
            },
            headlineContent = {
                Text("Switch Theme")
            },
            supportingContent = {
                Text("Toggle between day and night.")
            },
            trailingContent = {
                Switch(checked = isEnable, onCheckedChange = { onDarkThemeEnable(it) })
            }
        )
    }

}

@Composable
fun WordAndMeaningLanguagePrefs(
    selectedWordLanguage: WordLanguage,
    selectedMeaningLanguage: WordLanguage,
    onSelectWordLanguage: (WordLanguage) -> Unit,
    onSelectMeaningLanguage: (WordLanguage) -> Unit
) {
    val wordLanguages = remember { WordLanguage.entries.drop(1) }
    SettingContent(
        title = "Word & Meaning Language",
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                LoKDropdownMenu(
                    value = selectedWordLanguage.languageName,
                    items = wordLanguages,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_word_language))
                    },
                    itemContent = {
                        Text(it.languageName)
                    },
                    onItemSelected = onSelectWordLanguage
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                LoKDropdownMenu(
                    value = selectedMeaningLanguage.languageName,
                    items = wordLanguages,
                    label = {
                        Text(stringResource(R.string.home_screen_filter_select_meaning_language))
                    },
                    itemContent = {
                        Text(it.languageName)
                    },
                    onItemSelected = onSelectMeaningLanguage
                )
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}


@Composable
fun SettingContent(title: String, content: @Composable () -> Unit) {
    Text(title, modifier = Modifier.padding(8.dp), style = Typography.titleMedium)
    Column(
        Modifier.fillMaxWidth()
    ) {
        content()
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    LoKDictionaryTheme {
        SettingsScreen(
            uiState = SettingsUiState()
        ) { }
    }
}