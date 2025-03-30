package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UpIconButton(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = navigateUp
    ) { Icon(Icons.AutoMirrored.Default.ArrowBack, "back") }
}
