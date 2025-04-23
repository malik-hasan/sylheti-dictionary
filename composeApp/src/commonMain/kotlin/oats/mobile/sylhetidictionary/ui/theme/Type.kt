package oats.mobile.sylhetidictionary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import oats.mobile.sylhetidictionary.ui.app.LocalLanguage
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
import sylhetidictionary.composeapp.generated.resources.montserrat_var_wght
import sylhetidictionary.composeapp.generated.resources.montserrat_var_wght_italic
import sylhetidictionary.composeapp.generated.resources.noto_sans_bengali_var_wght_wdth
import sylhetidictionary.composeapp.generated.resources.tiro_bangla
import sylhetidictionary.composeapp.generated.resources.tiro_bangla_italic

val textLinkStyle: TextLinkStyles
    @Composable
    get() {
        val focusedStyle = SpanStyle(color = MaterialTheme.colorScheme.inversePrimary)
        return TextLinkStyles(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline),
            focusedStyle = focusedStyle,
            hoveredStyle = focusedStyle,
            pressedStyle = focusedStyle
        )
    }

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
        Font(Res.font.merriweather_black_italic, weight = FontWeight.Black, style = FontStyle.Italic)
    )

val latinDisplayFontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.montserrat_var_wght),
        Font(Res.font.montserrat_var_wght_italic, style = FontStyle.Italic),
        Font(Res.font.montserrat_var_wght, weight = FontWeight.Light),
        Font(Res.font.montserrat_var_wght_italic, weight = FontWeight.Light, style = FontStyle.Italic),
        Font(Res.font.montserrat_var_wght, weight = FontWeight.SemiBold),
        Font(Res.font.montserrat_var_wght_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
        Font(Res.font.montserrat_var_wght, weight = FontWeight.Bold),
        Font(Res.font.montserrat_var_wght_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(Res.font.montserrat_var_wght, weight = FontWeight.Black),
        Font(Res.font.montserrat_var_wght_italic, weight = FontWeight.Black, style = FontStyle.Italic)
    )

val bengaliBodyFontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.tiro_bangla),
        Font(Res.font.tiro_bangla_italic, style = FontStyle.Italic)
    )

val bengaliDisplayFontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.noto_sans_bengali_var_wght_wdth),
        Font(Res.font.noto_sans_bengali_var_wght_wdth, weight = FontWeight.Light),
        Font(Res.font.noto_sans_bengali_var_wght_wdth, weight = FontWeight.SemiBold),
        Font(Res.font.noto_sans_bengali_var_wght_wdth, weight = FontWeight.Bold),
        Font(Res.font.noto_sans_bengali_var_wght_wdth, weight = FontWeight.Black)
    )

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
            labelLarge = labelLarge.copy(fontFamily = displayFontFamily),
            labelMedium = labelMedium.copy(fontFamily = displayFontFamily),
            labelSmall = labelSmall.copy(fontFamily = displayFontFamily)
        )
    }
}
