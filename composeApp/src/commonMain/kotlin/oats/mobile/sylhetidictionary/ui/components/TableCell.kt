package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TableCell(
    topBorderThickness: Dp? = 1.dp,
    leftBorderThickness: Dp? = 1.dp,
    bottomBorderThickness: Dp? = null,
    rightBorderThickness: Dp? = null,
    backgroundColor: Color = Color.Unspecified,
    density: Density = LocalDensity.current,
    content: @Composable BoxScope.() -> Unit = {}
) = Box(
    modifier = Modifier
        .background(backgroundColor)
        .height(with(density) {
            MaterialTheme.typography.labelLarge.lineHeight.toDp() * 2
        }).fillMaxWidth(),
    contentAlignment = Alignment.Center
) {
    content()

    val borderColor = MaterialTheme.colorScheme.onBackground

    topBorderThickness?.let {
        HorizontalDivider(
            modifier = Modifier.align(Alignment.TopStart),
            color = borderColor,
            thickness = it
        )
    }

    leftBorderThickness?.let {
        VerticalDivider(
            modifier = Modifier.align(Alignment.TopStart),
            color = borderColor,
            thickness = it
        )
    }

    bottomBorderThickness?.let {
        HorizontalDivider(
            modifier = Modifier.align(Alignment.BottomStart),
            color = borderColor,
            thickness = it
        )
    }

    rightBorderThickness?.let {
        VerticalDivider(
            modifier = Modifier.align(Alignment.TopEnd),
            color = borderColor,
            thickness = it
        )
    }
}
