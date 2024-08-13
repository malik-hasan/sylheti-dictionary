package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import models.Locale

@Composable
actual fun SylhetiDictionaryTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    locale: Locale,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme else lightScheme,
        typography = getTypography(locale),
        content = content
    )
}
