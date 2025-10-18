package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.arrow_back

@Composable
fun BackIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = IconButton(
    modifier = modifier,
    onClick = onClick
) {
    Icon(
        painter = painterResource(Res.drawable.arrow_back),
        contentDescription = "Back"
    )
}
