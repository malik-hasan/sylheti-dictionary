package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VoicedCellBlocker() = Row {
    Spacer(Modifier.weight(1f))
    Box(Modifier
        .background(MaterialTheme.colorScheme.onBackground)
        .weight(1f)
        .fillMaxHeight()
    )
}
