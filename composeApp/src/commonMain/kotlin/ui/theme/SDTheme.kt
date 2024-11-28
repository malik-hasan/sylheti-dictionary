package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import models.Language
import ui.app.LocalLanguage

@Composable
expect fun SDTheme(
    dynamicTheme: Boolean,
    darkTheme: Boolean = isSystemInDarkTheme(),
    language: Language = LocalLanguage.current,
    content: @Composable () -> Unit
)
