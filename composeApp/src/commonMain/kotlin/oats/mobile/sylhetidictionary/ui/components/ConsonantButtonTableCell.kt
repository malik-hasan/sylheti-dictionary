package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ConsonantButtonTableCell(
    voicedOnly: Boolean = false,
    voicelessOnly: Boolean = false,
    leftBorderThickness: Dp? = 1.dp,
    bottomBorderThickness: Dp? = null,
    rightBorderThickness: Dp? = null,
    content: @Composable RowScope.() -> Unit
) = TableCell(
    leftBorderThickness = leftBorderThickness,
    bottomBorderThickness = bottomBorderThickness,
    rightBorderThickness = rightBorderThickness
) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (voicedOnly) ConsonantButtonSpacer()
        content()
        if (voicelessOnly) ConsonantButtonSpacer()
    }
}
