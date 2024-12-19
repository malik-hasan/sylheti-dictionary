package ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EntryDivider(modifier: Modifier) {
    HorizontalDivider(
        Modifier
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .widthIn(max = 500.dp)
            .then(Modifier)
    )
}
