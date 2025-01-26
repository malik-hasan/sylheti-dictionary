package oats.mobile.sylhetidictionary.ui.theme

import androidx.compose.runtime.Composable
import oats.mobile.sylhetidictionary.models.settings.Language
import oats.mobile.sylhetidictionary.ui.app.LocalLanguage

@Composable
expect fun SDTheme(
    dynamicTheme: Boolean,
    darkTheme: Boolean,
    language: Language = LocalLanguage.current,
    content: @Composable () -> Unit
)
