package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun SylhetiDictionaryTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    languageCode: String,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme else lightScheme,
        typography = getTypography(languageCode),
        content = content
    )
}
