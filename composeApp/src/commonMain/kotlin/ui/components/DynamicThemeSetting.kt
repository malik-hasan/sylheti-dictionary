package ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun DynamicThemeSetting(
    dynamicThemeEnabled: Boolean,
    onToggle: () -> Unit
)
