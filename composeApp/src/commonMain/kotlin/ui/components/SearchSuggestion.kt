package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ui.utils.SDString
import ui.utils.appendHighlighted

@Composable
fun SearchSuggestion(suggestion: SDString, onClick: () -> Unit, icon: @Composable () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        icon()
        Text(
            text = buildAnnotatedString {
                appendHighlighted(suggestion)
            },
            modifier = Modifier
                .clickable(onClick = onClick)
                .fillMaxWidth()
        )
    }
}
