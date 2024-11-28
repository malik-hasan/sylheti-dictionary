package ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun DynamicThemeSetting(
    dynamicThemeEnabled: Boolean,
    onToggle: () -> Unit
) {}
