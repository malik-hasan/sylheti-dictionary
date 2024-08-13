package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import di.LocalLocalization
import models.Locale

@Composable
expect fun SylhetiDictionaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    locale: Locale = LocalLocalization.current,
    content: @Composable () -> Unit
)
