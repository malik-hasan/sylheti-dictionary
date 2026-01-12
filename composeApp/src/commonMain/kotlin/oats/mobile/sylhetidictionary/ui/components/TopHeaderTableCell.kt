package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TopHeaderTableCell(
    label: String,
    rightBorderThickness: Dp? = null
) = HeaderTableCell(
    label = label,
    topBorderThickness = 2.dp,
    bottomBorderThickness = 1.dp,
    rightBorderThickness = rightBorderThickness
)
