package ui.theme

import androidx.compose.runtime.Composable
import models.settings.Language
import ui.app.LocalLanguage

@Composable
expect fun SDTheme(
    dynamicTheme: Boolean,
    darkTheme: Boolean,
    language: Language = LocalLanguage.current,
    content: @Composable () -> Unit
)
