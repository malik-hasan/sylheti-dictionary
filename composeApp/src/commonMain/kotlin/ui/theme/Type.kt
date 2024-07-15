package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.merriweather_black
import sylhetidictionary.composeapp.generated.resources.merriweather_black_italic
import sylhetidictionary.composeapp.generated.resources.merriweather_bold
import sylhetidictionary.composeapp.generated.resources.merriweather_bold_italic
import sylhetidictionary.composeapp.generated.resources.merriweather_italic
import sylhetidictionary.composeapp.generated.resources.merriweather_light
import sylhetidictionary.composeapp.generated.resources.merriweather_light_italic
import sylhetidictionary.composeapp.generated.resources.merriweather_regular
import sylhetidictionary.composeapp.generated.resources.montserrat
import sylhetidictionary.composeapp.generated.resources.montserrat_italic
import sylhetidictionary.composeapp.generated.resources.noto_serif_bengali


val bengaliFontFamily
    @Composable
    get() = FontFamily(Font(Res.font.noto_serif_bengali))

val bodyFontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.merriweather_regular),
        Font(Res.font.merriweather_italic, style = FontStyle.Italic),
        Font(Res.font.merriweather_light, weight = FontWeight.Light),
        Font(Res.font.merriweather_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
        Font(Res.font.merriweather_bold, weight = FontWeight.Bold),
        Font(Res.font.merriweather_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(Res.font.merriweather_black, weight = FontWeight.Black),
        Font(Res.font.merriweather_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    )

val displayFontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.montserrat),
        Font(Res.font.montserrat_italic, style = FontStyle.Italic)
    )

// Default Material 3 typography values
val baseline = Typography()

val AppTypography
    @Composable
    get() = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
