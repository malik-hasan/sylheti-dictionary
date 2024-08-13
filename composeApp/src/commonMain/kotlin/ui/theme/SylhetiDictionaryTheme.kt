package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import di.LocalLocalization

@Composable
expect fun SylhetiDictionaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    languageCode: String = LocalLocalization.current,
    content: @Composable () -> Unit
)
