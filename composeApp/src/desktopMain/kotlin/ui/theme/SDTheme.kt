package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import models.settings.Language

@Composable
actual fun SDTheme(
    dynamicTheme: Boolean,
    darkTheme: Boolean,
    language: Language,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme else lightScheme,
        typography = getTypography(language),
        content = content
    )
}
