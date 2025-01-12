package ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.ifTrue(condition: Boolean, modifier: @Composable Modifier.() -> Modifier) =
    if (condition) modifier() else this
