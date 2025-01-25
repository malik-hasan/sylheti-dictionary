package ui.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.dynamic_theme

@Composable
actual fun DynamicThemeSetting(
    dynamicThemeEnabled: Boolean,
    onToggle: () -> Unit
) = SwitchSetting(
    iconPainter = painterResource(Res.drawable.dynamic_theme),
    label = stringResource(Res.string.dynamic_theme),
    checked = dynamicThemeEnabled,
    onToggle = onToggle
)
