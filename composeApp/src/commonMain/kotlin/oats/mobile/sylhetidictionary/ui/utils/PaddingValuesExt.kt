package oats.mobile.sylhetidictionary.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun PaddingValues.horizontal(
    layoutDirection: LayoutDirection = LocalLayoutDirection.current
) = PaddingValues(
    start = calculateStartPadding(layoutDirection),
    end = calculateEndPadding(layoutDirection)
)
