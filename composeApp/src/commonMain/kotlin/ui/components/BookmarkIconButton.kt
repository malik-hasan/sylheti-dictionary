package ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bookmark
import sylhetidictionary.composeapp.generated.resources.bookmark_border

@Composable
fun BookmarkIconButton(isBookmark: Boolean, onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            painter = painterResource(
                with(Res.drawable) {
                    if (isBookmark) bookmark else bookmark_border
                }
            ),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "bookmark"
        )
    }
}
