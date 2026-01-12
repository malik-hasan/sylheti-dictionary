package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun VowelChartHeader(label: String) = Text(
    text = label,
    style = MaterialTheme.typography.labelLarge,
    color = MaterialTheme.colorScheme.onSecondaryContainer,
    modifier = Modifier
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .padding(4.dp)
)
