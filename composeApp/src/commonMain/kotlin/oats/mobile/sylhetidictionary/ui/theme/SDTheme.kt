package oats.mobile.sylhetidictionary.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import oats.mobile.sylhetidictionary.ui.app.LocalLanguage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SDTheme(
    colorScheme: ColorScheme,
    language: Language = LocalLanguage.current,
    content: @Composable () -> Unit
) = MaterialExpressiveTheme(
    colorScheme = colorScheme,
    typography = getTypography(language),
    content = content
)

@Composable
expect fun SDTheme(
    dynamicTheme: Boolean,
    darkTheme: Boolean,
    content: @Composable () -> Unit
)
