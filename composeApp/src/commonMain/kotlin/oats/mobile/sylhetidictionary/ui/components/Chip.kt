package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelMedium
) = Text(
    text = text,
    modifier = modifier
        .clip(MaterialTheme.shapes.extraSmall)
        .background(MaterialTheme.colorScheme.secondary)
        .padding(horizontal = 3.dp),
    style = style,
    color = MaterialTheme.colorScheme.onSecondary,
    fontWeight = FontWeight.SemiBold,
    fontFamily = latinDisplayFontFamily
)
