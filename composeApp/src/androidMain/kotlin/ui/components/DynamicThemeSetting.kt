package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import di.LocalDynamicTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.dynamic_theme

@Composable
actual fun DynamicThemeSetting(onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painterResource(Res.drawable.dynamic_theme),
            stringResource(Res.string.dynamic_theme),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(stringResource(Res.string.dynamic_theme), style = MaterialTheme.typography.bodyLarge)
    }

    Switch(
        checked = LocalDynamicTheme.current,
        onCheckedChange = onToggle
    )
}
