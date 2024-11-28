package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import ui.app.LocalLanguage
import models.Language
import org.jetbrains.compose.resources.stringResource
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinDisplayFontFamily

@Composable
fun RowScope.LanguageButton(
    language: Language,
    selectedLanguage: Language = LocalLanguage.current,
    onSelect: (Language) -> Unit
) {
    val selected = language == selectedLanguage
    val textColor = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified

    Column(
        modifier = Modifier
            .selectable(
                interactionSource = null,
                indication = null,
                selected = selected,
                onClick = { onSelect(language) }
            )
            .weight(1f)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = language.indicator.toString(),
            style = MaterialTheme.typography.displayLarge,
            fontFamily = if (language == Language.BN) bengaliBodyFontFamily else latinDisplayFontFamily,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
        Text(
            text = stringResource(language.label),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}
