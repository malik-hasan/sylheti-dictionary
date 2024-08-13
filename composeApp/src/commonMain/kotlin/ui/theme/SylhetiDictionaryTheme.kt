package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import di.LocalDynamicTheme
import di.LocalLocalization

@Composable
expect fun SylhetiDictionaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    languageCode: String = LocalLocalization.current,
    dynamicTheme: Boolean = LocalDynamicTheme.current,
    content: @Composable () -> Unit
)
