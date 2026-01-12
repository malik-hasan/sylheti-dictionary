package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HeaderTableCell(
    label: String,
    topBorderThickness: Dp? = 1.dp,
    leftBorderThickness: Dp? = 1.dp,
    bottomBorderThickness: Dp? = null,
    rightBorderThickness: Dp? = null
) = TableCell(
    topBorderThickness = topBorderThickness,
    leftBorderThickness = leftBorderThickness,
    rightBorderThickness = rightBorderThickness,
    bottomBorderThickness = bottomBorderThickness,
    backgroundColor = MaterialTheme.colorScheme.secondaryContainer
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}
