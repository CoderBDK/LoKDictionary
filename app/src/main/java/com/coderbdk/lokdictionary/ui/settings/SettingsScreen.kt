package com.coderbdk.lokdictionary.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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