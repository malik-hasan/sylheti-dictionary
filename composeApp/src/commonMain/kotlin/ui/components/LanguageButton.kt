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
import di.LocalLocalization
import models.Language
import org.jetbrains.compose.resources.stringResource
import ui.theme.bengaliBodyFontFamily
import ui.theme.englishDisplayFontFamily

@Composable
fun RowScope.LanguageButton(
    language: Language,
    locale: Language = LocalLocalization.current,
    onSelect: (Language) -> Unit
) {
    val selected = language == locale

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
            fontFamily = if (language == Language.BN) bengaliBodyFontFamily else englishDisplayFontFamily,
            fontWeight = FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
        Text(stringResource(language.label), style = MaterialTheme.typography.titleMedium)
    }
}
