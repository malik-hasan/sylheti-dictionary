package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import di.LocalDynamicTheme
import di.LocalLocalization
import models.Language

@Composable
expect fun SylhetiDictionaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    language: Language = LocalLocalization.current,
    dynamicTheme: Boolean = LocalDynamicTheme.current,
    content: @Composable () -> Unit
)
