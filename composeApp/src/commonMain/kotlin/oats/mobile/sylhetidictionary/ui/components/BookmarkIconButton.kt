package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bookmark
import sylhetidictionary.composeapp.generated.resources.bookmark_border

@Composable
fun BookmarkIconButton(
    isBookmark: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(onClick, modifier) {
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
