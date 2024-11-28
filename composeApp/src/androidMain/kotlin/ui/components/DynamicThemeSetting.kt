package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.dynamic_theme

@Composable
actual fun DynamicThemeSetting(
    dynamicThemeEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Column {
        SettingLabel(
            iconPainter = painterResource(Res.drawable.dynamic_theme),
            label = stringResource(Res.string.dynamic_theme),
        )

        Switch(
            checked = dynamicThemeEnabled,
            onCheckedChange = onToggle
        )
    }
}
