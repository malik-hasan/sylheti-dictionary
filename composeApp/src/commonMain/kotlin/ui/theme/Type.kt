package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import ui.app.LocalLanguage
import models.settings.Language
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
import sylhetidictionary.composeapp.generated.resources.noto_sans_bengali
import sylhetidictionary.composeapp.generated.resources.tiro_bangla
import sylhetidictionary.composeapp.generated.resources.tiro_bangla_italic

@Composable
fun getBodyFontFamily(language: Language = LocalLanguage.current) =
    if (language == Language.BN) bengaliBodyFontFamily else latinBodyFontFamily

@Composable
fun getDisplayFontFamily(language: Language = LocalLanguage.current) =
    if (language == Language.BN) bengaliDisplayFontFamily else latinDisplayFontFamily

val latinBodyFontFamily
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

val latinDisplayFontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.montserrat),
        Font(Res.font.montserrat_italic, style = FontStyle.Italic)
    )

val bengaliBodyFontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.tiro_bangla),
        Font(Res.font.tiro_bangla_italic, style = FontStyle.Italic)
    )

val bengaliDisplayFontFamily
    @Composable
    get() = FontFamily(Font(Res.font.noto_sans_bengali))

@Composable
fun getTypography(language: Language): Typography {

    val bodyFontFamily = getBodyFontFamily(language)
    val displayFontFamily = getDisplayFontFamily(language)

    return with(Typography()) {
        Typography(
            displayLarge = displayLarge.copy(fontFamily = displayFontFamily),
            displayMedium = displayMedium.copy(fontFamily = displayFontFamily),
            displaySmall = displaySmall.copy(fontFamily = displayFontFamily),
            headlineLarge = headlineLarge.copy(fontFamily = displayFontFamily),
            headlineMedium = headlineMedium.copy(fontFamily = displayFontFamily),
            headlineSmall = headlineSmall.copy(fontFamily = displayFontFamily),
            titleLarge = titleLarge.copy(fontFamily = displayFontFamily),
            titleMedium = titleMedium.copy(fontFamily = displayFontFamily),
            titleSmall = titleSmall.copy(fontFamily = displayFontFamily),
            bodyLarge = bodyLarge.copy(fontFamily = bodyFontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = bodyFontFamily),
            bodySmall = bodySmall.copy(fontFamily = bodyFontFamily),
            labelLarge = labelLarge.copy(fontFamily = bodyFontFamily),
            labelMedium = labelMedium.copy(fontFamily = bodyFontFamily),
            labelSmall = labelSmall.copy(fontFamily = bodyFontFamily)
        )
    }
}
