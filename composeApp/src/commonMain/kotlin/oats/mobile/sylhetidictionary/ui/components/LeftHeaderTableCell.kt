package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LeftHeaderTableCell(
    label: String,
    bottomBorderThickness: Dp? = null
) = HeaderTableCell(
    label = label,
    leftBorderThickness = 2.dp,
    rightBorderThickness = 1.dp,
    bottomBorderThickness = bottomBorderThickness
)
