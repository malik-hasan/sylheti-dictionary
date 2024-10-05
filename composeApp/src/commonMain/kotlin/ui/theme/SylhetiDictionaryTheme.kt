package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import di.LocalDynamicTheme
import di.LocalLanguage
import models.Language

@Composable
expect fun SylhetiDictionaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    language: Language = LocalLanguage.current,
    dynamicTheme: Boolean = LocalDynamicTheme.current,
    content: @Composable () -> Unit
)
