package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import models.Language

@Composable
actual fun SDTheme(
    darkTheme: Boolean,
    language: Language,
    dynamicTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme else lightScheme,
        typography = getTypography(language),
        content = content
    )
}
