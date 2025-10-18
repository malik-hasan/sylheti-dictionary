package oats.mobile.sylhetidictionary.ui.theme

import androidx.compose.runtime.Composable

@Composable
actual fun SDTheme(
    dynamicTheme: Boolean,
    darkTheme: Boolean,
    content: @Composable () -> Unit
) = SDTheme(
    colorScheme = if (darkTheme) darkScheme else lightScheme,
    content = content
)
