package ui.utils

import androidx.compose.ui.Modifier

fun Modifier.ifTrue(condition: Boolean, modifier: Modifier.() -> Modifier) =
    if (condition) modifier() else this
