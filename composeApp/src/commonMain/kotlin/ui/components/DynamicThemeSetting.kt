package ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun DynamicThemeSetting(onToggle: (Boolean) -> Unit)